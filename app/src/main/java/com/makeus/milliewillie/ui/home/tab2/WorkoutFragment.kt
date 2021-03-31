package com.makeus.milliewillie.ui.home.tab2

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.MyApplication.Companion.ROUTINE_ID_KEY_FROM_WORKOUT
import com.makeus.milliewillie.MyApplication.Companion.exerciseId
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutBinding
import com.makeus.milliewillie.databinding.WorkoutWeightRecyclerItemBinding
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.home.tab2.adapter.WorkoutRoutineAdapter
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartActivity.Companion.REPORT_DATE_KEY
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartActivity.Companion.START_ROUTINE_ID
import com.makeus.milliewillie.util.BasicTextFormat
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


@RequiresApi(Build.VERSION_CODES.O)
class WorkoutFragment :
    BaseDataBindingFragment<FragmentWorkoutBinding>(R.layout.fragment_workout) {

    private val viewModel by viewModel<WorkoutViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    private var goalValue: Float = 0f
    private var currentValue: Float = 0f

    private var isInputWeight by Delegates.notNull<Boolean>()
    private var isInputGoal by Delegates.notNull<Boolean>()

    private var now = LocalDate.now()
    private var postYear = repositoryCached.getPostYear()
    private var postMonth = repositoryCached.getPostMonth()
    private var postDay = repositoryCached.getPostDay()
    private var currentYear = now.year
    private var currentMonth = now.monthValue
    private var currentDay = now.dayOfMonth
    private val calendar = Calendar.getInstance()

    private val todayMonth = calendar.get(Calendar.MONTH) + 1
    private val today = calendar.get(Calendar.DAY_OF_MONTH)
    private lateinit var reportDate: String

    val dailyWeightArray = ArrayList<DailyWeight>()
    val weightDayArray = ArrayList<WeightDay>()
    var routineArray = ArrayList<MyRoutineInfo>()
    var routineId by Delegates.notNull<Long>()
    private var position by Delegates.notNull<Int>()

    lateinit var routineRecyclerAdapter: WorkoutRoutineAdapter
    private var routineItemList = ArrayList<MyRoutineInfo>()

    companion object {
        fun getInstance() = WorkoutFragment()
        var isModifiedRoutine: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!repositoryCached.getIsExerciseId()) {
            viewModel.apiRepository.postFirstEntrances()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isSuccess) {
                        Log.e("postFirstEntrances 호출 성공")

                        repositoryCached.setValue(LocalKey.EXERCISEID, it.result)
                        repositoryCached.setValue(LocalKey.ISEXERCISEID, true)
                    } else {
                        Log.e("postFirstEntrances 호출 실패")
                        Log.e(it.message)
                    }
                }.disposeOnDestroy(this)
        }

        isInputWeight = repositoryCached.getIsInputWeight()
        isInputGoal = repositoryCached.getIsInputGoal()
        exerciseId = repositoryCached.getExerciseId()

        Log.e("postYear currentYear = $postYear $currentYear")
        Log.e("postMonth currentMonth = $postMonth $currentMonth")
        Log.e("postDay currentDay = $postDay $currentDay")

        if (isInputWeight && postYear == currentYear && postMonth == currentMonth && postDay < currentDay) {
            isInputWeight = false
            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
        } else if (isInputWeight && postYear == currentYear && postMonth < currentMonth) {
            isInputWeight = false
            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
        } else if (isInputWeight && postYear < currentYear) {
            isInputWeight = false
            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
        }
    }

    override fun onResume() {
        super.onResume()
        // 루틴 GET 호출
        executeGetRoutines()
        // 체중 기록 GET 호출
        executeGetDailyWeight()

        setLineChart()
    }

    @SuppressLint("ResourceAsColor", "StringFormatMatches", "CheckResult")
    override fun FragmentWorkoutBinding.onBind() {
        vi= this@WorkoutFragment
        vm = viewModel

        todayDate() //오늘 날짜 설정
        reportDate = BasicTextFormat.BasicDateFormat(
            calendar.get(Calendar.YEAR).toString(),
            (calendar.get(Calendar.MONTH)+1).toString(),
            calendar.get(Calendar.DAY_OF_MONTH).toString()
        )

        onClickWeightDateItemAdd() // 체중 입력

        binding.workoutRecyclerDay.run {
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutWeightRecordDate>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutWeightRecordDate, WorkoutWeightRecyclerItemBinding>(
                        R.layout.workout_weight_recycler_item
                    ) {
                        vi = this@WorkoutFragment
                        item = it
                    }
                )
        }

    }

    @SuppressLint("SimpleDateFormat")
    fun executeGetRoutines() {
        val now = Date()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date = format.format(now)
        Log.e(date)

        routineArray.clear()

        viewModel.apiRepository.getRoutines(
            path = repositoryCached.getExerciseId(), targetDate = date
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess){
                    Log.e("getRoutines 호출 성공")

                    it.result.asJsonArray.forEach { objects ->
                        val item = objects.asJsonObject

                        routineArray.add(
                            MyRoutineInfo(
                                routineName = item.get("routineName").asString,
                                routineRepeatDay = item.get("routineRepeatDay").asString,
                                routineId = item.get("routineId").asLong,
                                isDoneRoutine = item.get("isDoneRoutine").asString
                            )
                        )
                    }
                    routineItemList = viewModel.createRoutineList(routineArray)
                } else {
                    Log.e("getRoutines 호출 실패")
                    Log.e(it.message)
                }
                setRecyclerAdapter()
            }.disposeOnDestroy(this)
    }

    private fun setRecyclerAdapter() {
        Log.e("routineItemList = $routineItemList")
        routineRecyclerAdapter = WorkoutRoutineAdapter(context, routineItemList)
        binding.workoutRecyclerTodayRoutine.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)

            // 키워드 아이템 클릭 리스너
            routineRecyclerAdapter.let {
                it.setRoutineItemClickListener(object : WorkoutRoutineAdapter.RoutineItemClickListener {
                    override fun onItemClick(position: Int) {
                        Log.e(position.toString())
                        this@WorkoutFragment.position = position

                        onClickItem(3)
                    }
                })
            } // end listener
            adapter = routineRecyclerAdapter
        }

    }

    private fun executeGetDailyWeight() {
        dailyWeightArray.clear()
        weightDayArray.clear()
        viewModel.getDailyWeight()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.e(it.isSuccess.toString())
                if (it.isSuccess) {
                    Log.e("getDailyWeight 호출 성공")

                    val goalText = String.format(
                        getString(
                            R.string.goal_weight_var,
                            it.result.goalWeight
                        )
                    )
                    viewModel.goalWeightText.postValue(goalText)
                    binding.workoutLayoutGoalWeight.visibility = View.VISIBLE

                    goalValue = it.result.goalWeight.toFloat()

                    it.result.dailyWeightList.forEach { element ->
                        dailyWeightArray.add(0, DailyWeight(element.asString))
                    }
                    it.result.weightDayList.forEach { element ->
                        weightDayArray.add(0, WeightDay(element.asString))
                    }
                    viewModel.createWeightItem(dailyWeightArray, weightDayArray)

                    Handler().postDelayed({
                        setLineChart()
                    }, 200)
                } else {
                    Log.e("getDailyWeight 호출 실패")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this@WorkoutFragment)
    }

    private fun todayDate(){
        val dateInstance = Calendar.getInstance()

        val month = dateInstance.get(Calendar.MONTH) + 1
        val day = dateInstance.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = dateInstance.get(Calendar.DAY_OF_WEEK)
        var dayOfWeekText = ""

        when (dayOfWeek) {
            1 -> dayOfWeekText = "일"
            2 -> dayOfWeekText = "월"
            3 -> dayOfWeekText = "화"
            4 -> dayOfWeekText = "수"
            5 -> dayOfWeekText = "목"
            6 -> dayOfWeekText = "금"
            7 -> dayOfWeekText = "토"
        }

        val today = String.format(getString(R.string.todayDateFormWithDayOfWeek, month, day, dayOfWeekText))

        viewModel.liveDataToday.postValue(today)
    }

    private fun executePostDailyWeight() {
        WeightAddRecordBottomSheetFragment.getInstance()
            .setOnClickOk { weight ->
                viewModel.apiRepository.postDailyWeight(
                    body = PostDailyWeightRequest(dayWeight = weight.toDouble()),
                    path = repositoryCached.getExerciseId()
                )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.e(it.isSuccess.toString())
                        if (it.isSuccess) {
                            Log.e("호출 성공")

                            isInputWeight = true
                            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)

                            postYear = currentYear
                            postMonth = currentMonth
                            postDay = currentDay
                            repositoryCached.setValue(LocalKey.POSTYEAR, postYear)
                            repositoryCached.setValue(LocalKey.POSTMONTH, postMonth)
                            repositoryCached.setValue(LocalKey.POSTDAY, postDay)

                            drawDailyWeight(goalValue.toString(), weight)
                        } else {
                            Log.e("호출 실패")
                            Log.e(it.message)
                        }
                    }.disposeOnDestroy(this)
            }.show(fragmentManager!!)
    }

    private fun executePostFirstWeight() {
        WeightRecordBottomSheetFragment.getInstance()
            .setOnClickOk { goal, current ->
                viewModel.apiRepository.postFirstWeight(
                    exerciseId = repositoryCached.getExerciseId(),
                    body = FirstWeightRequest(goalWeight = goal.toDouble(), firstWeight = current.toDouble()))
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.e(it.isSuccess.toString())
                        if (it.isSuccess) {
                            Log.e("postFirstWeight 성공")

                            if (goal != "-1.0") goalValue = goal.toFloat()
                            isInputWeight = true
                            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
                            repositoryCached.setValue(LocalKey.ISINPUTGOAL, true)

                            postYear = currentYear
                            postMonth = currentMonth
                            postDay = currentDay
                            repositoryCached.setValue(LocalKey.POSTYEAR, postYear)
                            repositoryCached.setValue(LocalKey.POSTMONTH, postMonth)
                            repositoryCached.setValue(LocalKey.POSTDAY, postDay)

                            drawDailyWeight(goal, current)
                        } else {
                            Log.e("postFirstWeight 실패")
                            Log.e(it.message)
                        }
                    }.disposeOnDestroy(this)
            }.show(fragmentManager!!)
    }

    fun onClickWeightDateItemAdd() {
        // 목표체중 유무에 따라 다른 창을 띄움
        when (isInputGoal) {
            true -> if (!isInputWeight) executePostDailyWeight()
            false -> executePostFirstWeight()
        }

    }

    private fun drawDailyWeight(goal: String, current: String) {
        Log.e("called drawDailyWeight")
        var goalText = ""
        if (goal != "-1.0") goalText = String.format(getString(R.string.goal_weight_var, goal))

        if (current != "-1.0"){
            viewModel.goalWeightText.postValue(goalText)
            binding.workoutLayoutGoalWeight.visibility = View.VISIBLE
            if (todayMonth < 10) {
                val dateform = String.format(getString(R.string.date_weight_record_format, "0$todayMonth", today))
                viewModel.addWeightItem(WorkoutWeightRecordDate(weight = current, date = dateform))
            } else {
                val dateform = String.format(getString(R.string.date_weight_record_format, todayMonth.toString(), today))
                viewModel.addWeightItem(WorkoutWeightRecordDate(weight = current, date = dateform))
            }

            Handler().postDelayed({
                setLineChart()
            }, 200)
        }
    }

    private fun setLineChart() {
        val values = ArrayList<Entry>()
        val goalWeight = ArrayList<Entry>()

        for(i in 0 until viewModel.liveRecordWeightItemListSize){
            values.add(
                Entry(
                    i.toFloat(),
                    viewModel.liveRecordWeightItemList.value!![i].weight.toFloat()
                )
            ) // (x값, y값) // (list size, weight)
        }

        if (goalValue.toInt() != 0) {
            goalWeight.add(Entry(0f, goalValue))
            goalWeight.add(Entry(5f, goalValue))
        }

        val set1 = LineDataSet(values, "DataSet 1")
        val set2 = LineDataSet(goalWeight, "GOAL")

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1) // 체중에 대한 data sets
        dataSets.add(set2) // 목표에 대한 data sets

        set1.apply {
            color = Color.WHITE
            setCircleColor(Color.WHITE)
            valueTextSize = 0.0f
            setDrawHighlightIndicators(false)
        }

        set2.apply {
            color = Color.GRAY
            valueTextSize = 0.0f
            setDrawCircles(false)
            setDrawHighlightIndicators(false)
        }

        // create a data object with the data sets

        val xAxis = binding.workoutLayoutWeightGraph.xAxis
        xAxis.apply {
            setDrawGridLines(false) // 그리드라인 세로선 설정
            setDrawAxisLine(false) // 그리드라인 가로선 설정
            setDrawLabels(false) // 라벨 유무 설정
        }

        binding.workoutLayoutWeightGraph.apply {
            axisRight.isEnabled = false // x충 오른쪽 데이터 설정
            axisLeft.isEnabled = false // x축 왼쪽 데이터 설정

            setNoDataText(getString(R.string.chart_no_data_text))
            setNoDataTextColor(R.color.white)
            description.text = "" // 차트 설명 설정
            setPinchZoom(false) // 차트 확대 설정
            isDoubleTapToZoomEnabled = false // 더블탭으로 확대 설정

            notifyDataSetChanged() // 차트변경 알림

            // 범례
            legend.apply {
                setDrawInside(false)
                isEnabled = false
            }
        }

        val data = LineData(dataSets)
        // set data
        binding.workoutLayoutWeightGraph.setData(data)
        binding.workoutLayoutWeightGraph.notifyDataSetChanged()
        binding.workoutLayoutWeightGraph.invalidate()
    }

    fun onClickItem(distinction: Int) {
        when (distinction) {
            1 -> { // 체중기록 화면
                ActivityNavigator.with(this).weightRecord().start()
            }
            2 -> { // 오늘의 운동 화면
                ActivityNavigator.with(this).todayWorkout().start()
            }
            3 -> {
                when (routineArray[position].isDoneRoutine.toBoolean()) {
                    true -> { // 운동 리포트 화면
                        ActivityNavigator.with(this).reports().apply {
                            putExtra(START_ROUTINE_ID, routineArray[position].routineId)
                            putExtra(REPORT_DATE_KEY, reportDate)
                            start()
                        }
                    }
                    false -> { // 운동 시작 화면
                        ActivityNavigator.with(this).workoutStart().apply {
                            putExtra(ROUTINE_ID_KEY_FROM_WORKOUT, routineArray[position].routineId)
                            start()
                        }
                    }
                }

            }
            4 -> { // 루틴 만들기 화면
                isModifiedRoutine = false
                ActivityNavigator.with(context!!).routine().start()
            }
        }
    }

//    override fun onBackPressed(): Boolean {
//        Log.e("onBackPressed in workout")
//        if (!isInputGoal) executePostFirstWeight()
//        return super.onBackPressed()
//    }

}