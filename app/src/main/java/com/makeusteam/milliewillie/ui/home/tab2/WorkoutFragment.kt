package com.makeusteam.milliewillie.ui.home.tab2

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
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.fragment.BaseDataBindingFragment
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.MyApplication.Companion.ROUTINE_ID_KEY_FROM_WORKOUT
import com.makeusteam.milliewillie.MyApplication.Companion.globalApplicationContext
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.FragmentWorkoutBinding
import com.makeusteam.milliewillie.databinding.WorkoutWeightRecyclerItemBinding
import com.makeusteam.milliewillie.model.*
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.SampleToast
import com.makeusteam.milliewillie.ui.home.tab2.adapter.WorkoutRoutineAdapter
import com.makeusteam.milliewillie.ui.workoutStart.WorkoutStartActivity.Companion.REPORT_DATE_KEY
import com.makeusteam.milliewillie.ui.workoutStart.WorkoutStartActivity.Companion.START_ROUTINE_ID
import com.makeusteam.milliewillie.util.BasicTextFormat
import com.makeusteam.milliewillie.util.ConvertTimeMills
import com.makeusteam.milliewillie.util.Log
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
        @Synchronized
        fun getInstance() = WorkoutFragment()
        var isModifiedRoutine: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("ererciseId = ${repositoryCached.getExerciseId()}")
        // ExerciseId??? ?????? 1??? POST??? ?????????, ?????? ????????????, ???????????? ??? ????????? ???????????? ?????? ??????
        if (repositoryCached.getExerciseId() == (-1).toLong()) {
            viewModel.apiRepository.postFirstEntrances()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isSuccess) {
                        Log.e("postFirstEntrances ?????? ??????")
                        // ExId ????????? ??????
                        repositoryCached.setValue(LocalKey.EXERCISEID, it.result)
                    } else {
                        Log.e("postFirstEntrances ?????? ??????")
                        Log.e(it.message)
                    }
                }.disposeOnDestroy(this)
        }

        isInputWeight = repositoryCached.getIsInputWeight()
        isInputGoal = repositoryCached.getIsInputGoal()

        val postDateMillis = ConvertTimeMills.ConvertDateToMillis(postYear, postMonth, postDay)
        val currentDateMillis = ConvertTimeMills.ConvertDateToMillis(currentYear, currentMonth, currentDay)

        Log.e("postDateMillis = $postDateMillis")
        Log.e("currentDateMillis = $currentDateMillis")
        Log.e("currentDateMillis ??? ??????? ${postDateMillis < currentDateMillis}")

        if (postDateMillis < currentDateMillis) {
            isInputWeight = false
            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
        }
//        if (isInputWeight && postYear == currentYear && postMonth == currentMonth && postDay < currentDay) { // ????????? ??????
//            isInputWeight = false
//            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
//        } else if (isInputWeight && postYear == currentYear && postMonth < currentMonth) { // ?????? ???????????? ???, ?????? ??????
//            isInputWeight = false
//            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
//        } else if (isInputWeight && postYear < currentYear) { // ????????? ???????????? ???, ????????? ??????
//            isInputWeight = false
//            repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
//        }
    }

    override fun onResume() {
        super.onResume()
        // ?????? GET ??????
        executeGetRoutines()
        // ?????? ?????? GET ??????
        executeGetDailyWeight()

        setLineChart()
    }

    @SuppressLint("ResourceAsColor", "StringFormatMatches", "CheckResult")
    override fun FragmentWorkoutBinding.onBind() {
        vi= this@WorkoutFragment
        vm = viewModel

        binding.workoutFab.size

        todayDate() //?????? ?????? ??????
        reportDate = BasicTextFormat.BasicDateFormat(
            calendar.get(Calendar.YEAR).toString(),
            (calendar.get(Calendar.MONTH)+1).toString(),
            calendar.get(Calendar.DAY_OF_MONTH).toString()
        )

        onClickWeightDateItemAdd() // ?????? ??????

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
                    Log.e("getRoutines ?????? ??????")

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
                    Log.e("getRoutines ?????? ??????")
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

            // ????????? ????????? ?????? ?????????
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
        var goalValueText = ""
        viewModel.getDailyWeight()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.e(it.isSuccess.toString())
                if (it.isSuccess) {
                    Log.e("getDailyWeight ?????? ??????")

                    goalValueText = if (it.result.goalWeight == "-1.0") "" else it.result.goalWeight

                    val goalText = String.format(
                        globalApplicationContext.getString(
                            R.string.goal_weight_var,
                            goalValueText
                        )
                    )
                    if (goalValueText != "") {
                        viewModel.goalWeightText.postValue(goalText)
                        binding.workoutLayoutGoalWeight.visibility = View.VISIBLE
                    }

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
                    Log.e("getDailyWeight ?????? ??????")
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
            1 -> dayOfWeekText = "???"
            2 -> dayOfWeekText = "???"
            3 -> dayOfWeekText = "???"
            4 -> dayOfWeekText = "???"
            5 -> dayOfWeekText = "???"
            6 -> dayOfWeekText = "???"
            7 -> dayOfWeekText = "???"
        }

        val today = String.format(globalApplicationContext.getString(R.string.todayDateFormWithDayOfWeek, month, day, dayOfWeekText))

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
                            Log.e("?????? ??????")

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
                            Log.e("?????? ??????")
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
                            Log.e("postFirstWeight ??????")

                            if (goal != "-1.0") {
                                goalValue = goal.toFloat()
                                isInputWeight = true
                                repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, isInputWeight)
                            }
                            isInputGoal = true
                            repositoryCached.setValue(LocalKey.ISINPUTGOAL, true)

                            postYear = currentYear
                            postMonth = currentMonth
                            postDay = currentDay
                            repositoryCached.setValue(LocalKey.POSTYEAR, postYear)
                            repositoryCached.setValue(LocalKey.POSTMONTH, postMonth)
                            repositoryCached.setValue(LocalKey.POSTDAY, postDay)

                            drawDailyWeight(goal, current)
                        } else {
                            Log.e("postFirstWeight ??????")
                            Log.e(it.message)
                        }
                    }.disposeOnDestroy(this)
            }.show(fragmentManager!!)
    }

    fun onClickWeightDateItemAdd() {
        Log.e("repositoryCached.getIsInputGoal() = ${repositoryCached.getIsInputGoal()}")
        Log.e("!isInputWeight = ${!isInputWeight}")
        // ???????????? ????????? ?????? ?????? ?????? ??????
        when (repositoryCached.getIsInputGoal()) {
            true -> {
                if (!isInputWeight) executePostDailyWeight()
                else SampleToast.createToast(context!!, "????????? ????????? ??? ??? ????????? ??? ????????????")?.show()
            }
            false -> executePostFirstWeight()
        }

    }

    private fun drawDailyWeight(goal: String, current: String) {
        Log.e("called drawDailyWeight")
        var goalText = ""
        if (goal != "-1.0") goalText = String.format(globalApplicationContext.getString(R.string.goal_weight_var, goal))

        if (current != "-1.0"){
            viewModel.goalWeightText.postValue(goalText)
            binding.workoutLayoutGoalWeight.visibility = View.VISIBLE
            if (todayMonth < 10) {
                val dateform = String.format(globalApplicationContext.getString(R.string.date_weight_record_format, "0$todayMonth", today))
                viewModel.addWeightItem(WorkoutWeightRecordDate(weight = current, date = dateform))
            } else {
                val dateform = String.format(globalApplicationContext.getString(R.string.date_weight_record_format, todayMonth.toString(), today))
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
            ) // (x???, y???) // (list size, weight)
        }

        if (goalValue.toInt() != 0) {
            goalWeight.add(Entry(0f, goalValue))
            goalWeight.add(Entry(5f, goalValue))
        }

        val set1 = LineDataSet(values, "DataSet 1")
        val set2 = LineDataSet(goalWeight, "GOAL")

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1) // ????????? ?????? data sets
        dataSets.add(set2) // ????????? ?????? data sets

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
            setDrawGridLines(false) // ??????????????? ????????? ??????
            setDrawAxisLine(false) // ??????????????? ????????? ??????
            setDrawLabels(false) // ?????? ?????? ??????
        }

        binding.workoutLayoutWeightGraph.apply {
            axisRight.isEnabled = false // x??? ????????? ????????? ??????
            axisLeft.isEnabled = false // x??? ?????? ????????? ??????

            setNoDataText(globalApplicationContext.getString(R.string.chart_no_data_text))
            setNoDataTextColor(R.color.white)
            description.text = "" // ?????? ?????? ??????
            setPinchZoom(false) // ?????? ?????? ??????
            isDoubleTapToZoomEnabled = false // ??????????????? ?????? ??????

            notifyDataSetChanged() // ???????????? ??????

            // ??????
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
            1 -> { // ???????????? ??????
                ActivityNavigator.with(this).weightRecord().start()
            }
            2 -> { // ????????? ?????? ??????
                ActivityNavigator.with(this).todayWorkout().start()
            }
            3 -> {
                when (routineArray[position].isDoneRoutine.toBoolean()) {
                    true -> { // ?????? ????????? ??????
                        ActivityNavigator.with(this).reports().apply {
                            putExtra(START_ROUTINE_ID, routineArray[position].routineId)
                            putExtra(REPORT_DATE_KEY, reportDate)
                            start()
                        }
                    }
                    false -> { // ?????? ?????? ??????
                        ActivityNavigator.with(this).workoutStart().apply {
                            putExtra(ROUTINE_ID_KEY_FROM_WORKOUT, routineArray[position].routineId)
                            start()
                        }
                    }
                }

            }
            4 -> { // ?????? ????????? ??????
                isModifiedRoutine = false
                ActivityNavigator.with(context!!).routine().start()
            }
        }
    }


}