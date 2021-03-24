package com.makeus.milliewillie.ui.home.tab2

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.databinding.WorkoutWeightRecyclerItemBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class WorkoutFragment :
    BaseDataBindingFragment<FragmentWorkoutBinding>(R.layout.fragment_workout) {

    private val viewModel by viewModel<WorkoutViewModel>()

    private var goalValue: Float = 0f
    private var currentValue: Float = 0f

    val calendar = Calendar.getInstance()

    val todayMonth = calendar.get(Calendar.MONTH) + 1
    val today = calendar.get(Calendar.DAY_OF_MONTH)

    val dailyWeightArray = ArrayList<DailyWeight>()
    val weightDayArray = ArrayList<WeightDay>()
    var routineArray = ArrayList<MyRoutineInfo>()

    companion object {
        fun getInstance() = WorkoutFragment()
        const val IS_GOAL = "IS_GOAL"
        const val EXERCISE_ID = "EXERCISE_ID"
        var isInputGoal = SharedPreference.getSettingBooleanItem(IS_GOAL)
        var exerciseId: Long = SharedPreference.getSettingItem(EXERCISE_ID)?.toLong() ?: 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isInputGoal = SharedPreference.getSettingBooleanItem(IS_GOAL)
        exerciseId = SharedPreference.getSettingItem(EXERCISE_ID)?.toLong() ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor", "StringFormatMatches", "CheckResult")
    override fun FragmentWorkoutBinding.onBind() {
        vi= this@WorkoutFragment
        vm = viewModel

        todayDate() //오늘 날짜 설정

//        isInputGoal = true
//        SharedPreference.putSettingBooleanItem(IS_GOAL, isInputGoal)

        // 체중 기록 GET 호출
        executeGetWeightRecord()
        // 루틴 GET 호출
        executeGetRoutines()

        onClickWeightDateItemAdd() // 체중 입력

        //라인차트 함수 호출
        setLineChart()

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

        binding.workoutRecyclerTodayRoutine.run {
            adapter = BaseDataBindingRecyclerViewAdapter<MyRoutineInfo>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MyRoutineInfo, WorkoutRoutineRecyclerItemBinding>(
                        R.layout.workout_routine_recycler_item
                    ) {
                        vi = this@WorkoutFragment
                        item = it
                    }
                )
        }


    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun executeGetRoutines() {
        val now = Date()
        val format = SimpleDateFormat("yyyy-MM-dd")
        val date = format.format(now)
        Log.e(date)

        viewModel.apiRepository.getRoutines(
            path = SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong(), targetDate = date
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
                                routineId = item.get("routineId").asLong
                            )
                        )
                    }
                    viewModel.createRoutineList(routineArray)
                } else {
                    Log.e("getRoutines 호출 실패")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this)
    }


    fun executeGetWeightRecord() {
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
                    Log.e("goalValue: $goalValue")
                    Log.e("dailyWeightList: ${it.result.dailyWeightList}")
                    Log.e("weightDayList: ${it.result.weightDayList}")
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

    fun todayDate(){
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

        val today = String.format(getString(R.string.todayDateForm, month, day, dayOfWeekText))

        viewModel.liveDataToday.postValue(today)
    }

    fun onClickWeightDateItemAdd() {
        // 목표체중 유무에 따라 다른 창을 띄움
        when (isInputGoal) {
            true -> {
                WeightAddRecordBottomSheetFragment.getInstance()
                    .setOnClickOk { weight ->
                        viewModel.apiRepository.postDailyWeight(
                            body = PostDailyWeightRequest(
                                dayWeight = weight.toDouble()
                            ),
                            path = SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong()
                        )
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                Log.e(it.isSuccess.toString())
                                if (it.isSuccess) {
                                    Log.e("호출 성공")

                                    drawDailyWeight(goalValue.toString(), weight)

                                    Handler().postDelayed({
                                        setLineChart()
                                    }, 200)

                                    "호출 성공".showShortToastSafe()
                                } else {
                                    Log.e("호출 실패")
                                    Log.e(it.message)
                                    "호출 실패".showShortToastSafe()
                                }
                            }.disposeOnDestroy(this)
                    }.show(fragmentManager!!)
            }
            false -> {
                WeightRecordBottomSheetFragment.getInstance()
                    .setOnClickOk { goal, current ->
                        viewModel.apiRepository.postFirstWeight(
                            FirstWeightRequest(
                                goalWeight = goal.toInt(),
                                firstWeight = current.toInt()
                            )
                        )
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe {
                                Log.e(it.isSuccess.toString())
                                if (it.isSuccess) {
                                    Log.e("postFirstWeight 성공")
                                    Log.e(it.isSuccess.toString())
                                    Log.e(it.code.toString())
                                    Log.e(it.message)

                                    drawDailyWeight(goal, current)

                                    goalValue = goal.toFloat()
                                    isInputGoal = true
                                    SharedPreference.putSettingBooleanItem(IS_GOAL, isInputGoal)
                                    SharedPreference.putSettingItem(
                                        EXERCISE_ID,
                                        it.result.exerciseId.toString()
                                    )

                                    Handler().postDelayed({
                                        setLineChart()
                                    }, 200)
                                    "호출 성공".showShortToastSafe()
                                } else {
                                    Log.e("postFirstWeight 실패")
                                    Log.e(it.message)
                                    "호출 실패".showShortToastSafe()
                                }
                            }.disposeOnDestroy(this)
                    }.show(fragmentManager!!)
            }
        }

    }

    fun drawDailyWeight(goal: String, current: String) {
        Log.e("called drawDailyWeight")
        val goalText = String.format(getString(R.string.goal_weight_var, goal))
        viewModel.goalWeightText.postValue(goalText)
        binding.workoutLayoutGoalWeight.visibility = View.VISIBLE

        if (todayMonth < 10) {
            val dateform = String.format(
                getString(
                    R.string.date_weight_record_format,
                    "0$todayMonth",
                    today
                )
            )
            viewModel.addWeightItem(WorkoutWeightRecordDate(weight = current, date = dateform))
        } else {
            val dateform = String.format(
                getString(
                    R.string.date_weight_record_format,
                    todayMonth.toString(),
                    today
                )
            )
            viewModel.addWeightItem(WorkoutWeightRecordDate(weight = current, date = dateform))
        }
    }

    fun setLineChart() {
        Log.e("line chart called")
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
            3 -> { // 운동 시작 화면
                ActivityNavigator.with(this).workoutStart().start()
            }
            4 -> { // 루틴 만들기 화면
                ActivityNavigator.with(context!!).routine().start()
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

}