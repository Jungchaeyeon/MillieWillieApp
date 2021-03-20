package com.makeus.milliewillie.ui.home.tab2

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Handler
import android.view.View
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.databinding.WorkoutWeightRecyclerItemBinding
import com.makeus.milliewillie.model.TodayRoutines
import com.makeus.milliewillie.model.WorkoutWeightRecordDate
import com.makeus.milliewillie.ui.home.tab1.HomeFragment
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList


class WorkoutFragment :
    BaseDataBindingFragment<FragmentWorkoutBinding>(R.layout.fragment_workout) {

    private val viewModel by viewModel<WorkoutViewModel>()

    private var goalValue: Float = 0f
    private var currentValue: Float = 0f

    val calendar = Calendar.getInstance()

    val todayMonth = calendar.get(Calendar.MONTH)
    val today = calendar.get(Calendar.DAY_OF_MONTH)

    val weightItemList = ArrayList<String>()

    companion object {
        fun getInstance() = WorkoutFragment()
        const val IS_GOAL = "IS_GOAL"
        var isInputGoal = SharedPreference.getSettingBooleanItem(IS_GOAL)
    }

    @SuppressLint("ResourceAsColor", "StringFormatMatches")
    override fun FragmentWorkoutBinding.onBind() {
        vi= this@WorkoutFragment
        vm = viewModel

        isInputGoal = false
        SharedPreference.putSettingBooleanItem(IS_GOAL, isInputGoal)

        // 목표체중 유무에 따라 다른 창을 띄움
        when (isInputGoal) {
            true -> {
                WeightAddRecordBottomSheetFragment.getInstance()
                    .setOnClickOk { weight ->
                        Log.e(weight)
                    }.show(fragmentManager!!)
            }
            false -> {
                WeightRecordBottomSheetFragment.getInstance()
                    .setOnClickOk { goal, current ->
                        val goalText = String.format(getString(R.string.goal_weight_var, goal))
                        viewModel.goalWeightText.postValue(goalText)
                        binding.workoutDash.visibility = View.VISIBLE

                        goalValue = goal.toFloat()
                        currentValue = current.toFloat()
                        isInputGoal = true
                        SharedPreference.putSettingBooleanItem(IS_GOAL, isInputGoal)

                        val dateform = String.format(getString(R.string.date_weight_record_format, todayMonth, today))
                        viewModel.addWeightItem(WorkoutWeightRecordDate(weight = current, date = dateform))

                        Handler().postDelayed ({
                            setLineChart()
                        },200)
                    }.show(fragmentManager!!)
            }
        }

        //라인차트 함수 호출
        setLineChart()

        binding.workoutRecyclerDay.run {
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutWeightRecordDate>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutWeightRecordDate, WorkoutWeightRecyclerItemBinding>(R.layout.workout_weight_recycler_item) {
                        vi = this@WorkoutFragment
                        item = it
                    }
                )
        }

        binding.workoutRecyclerTodayRoutine.run {
            adapter = BaseDataBindingRecyclerViewAdapter<TodayRoutines>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<TodayRoutines, WorkoutRoutineRecyclerItemBinding>(R.layout.workout_routine_recycler_item){
                        vi = this@WorkoutFragment
                        item = it
                    }
                )
        }


    }

    @SuppressLint("StringFormatMatches")
    fun onClickWeightDateItemAdd() {
        WeightAddRecordBottomSheetFragment.getInstance()
            .setOnClickOk { weight ->
                Log.e(weight)
                val dateform = String.format(getString(R.string.date_weight_record_format, todayMonth, today))
                viewModel.addWeightItem(WorkoutWeightRecordDate(weight = weight, date = dateform))

                Handler().postDelayed ({
                    setLineChart()
                },200)
            }.show(fragmentManager!!)
    }

    fun setLineChart() {
        Log.e("line chart called")
        val values = ArrayList<Entry>()
        val goalWeight = ArrayList<Entry>()

        for(i in 0 until viewModel.liveRecordWeightItemListSize){
            Log.e("${viewModel.liveRecordWeightItemList.value!![i].weight.toInt().toFloat()}")
            values.add(Entry (i.toFloat(), viewModel.liveRecordWeightItemList.value!![i].weight.toInt().toFloat())) // (x값, y값) // (list size, weight)
        }

        goalWeight.add(Entry(0f, goalValue))
        goalWeight.add(Entry(5f, goalValue))

        val xAxis = binding.workoutLayoutWeightGraph.xAxis

        val set1 = LineDataSet(values, "DataSet 1")
        val set2 = LineDataSet(goalWeight, "GOAL")

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1) // add the data sets
        dataSets.add(set2)

        // create a data object with the data sets
        val data = LineData(dataSets)

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


        // set data
        binding.workoutLayoutWeightGraph.setData(data)
        binding.workoutLayoutWeightGraph.notifyDataSetChanged()
        binding.workoutLayoutWeightGraph.invalidate()
    }

    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()?.replace(
            R.id.container,
            nextFrag,
            "findThisFragment"
        )
            ?.addToBackStack(null)?.commit()
    }

    fun onClick() {
        ActivityNavigator.with(context!!).routine().start()
    }

    override fun onResume() {
        super.onResume()
        viewModel.defaultRecordWeightItemList()
    }

}