package com.makeus.milliewillie.ui.home.tab2

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayBirthRecyclerItemBinding
import com.makeus.milliewillie.databinding.FragmentWorkoutBinding
import com.makeus.milliewillie.databinding.WorkoutTodayExRecyclerItemBinding
import com.makeus.milliewillie.model.DdayCheckList
import com.makeus.milliewillie.model.TodayExDayOfWeek
import com.makeus.milliewillie.ui.home.tab1.HomeFragment
import org.koin.android.viewmodel.ext.android.viewModel


class WorkoutFragment :
    BaseDataBindingFragment<FragmentWorkoutBinding>(R.layout.fragment_workout) {

    private val viewModel by viewModel<WorkoutViewModel>()

    companion object {
        fun getInstance() = WorkoutFragment()
    }

    @SuppressLint("ResourceAsColor")
    override fun FragmentWorkoutBinding.onBind() {
        vi= this@WorkoutFragment
        vm = viewModel

        WeightRecordBottomSheetFragment.getInstance()
            .setOnClickOk { goal, current ->

            }.show(fragmentManager!!)

        setLineChart()

        binding.workoutRecyclerDayOfWeek.run {
            adapter = BaseDataBindingRecyclerViewAdapter<TodayExDayOfWeek>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<TodayExDayOfWeek, WorkoutTodayExRecyclerItemBinding>(R.layout.workout_today_ex_recycler_item) {
                        vi = this@WorkoutFragment
                        item = it
                    }
                )
        }



    }

    fun setLineChart() {
        val values = ArrayList<Entry>()
        for(i in 0..9){
            val dot = (Math.random() * 10).toFloat()
            values.add(Entry (i.toFloat(), dot)) // (x값, y값) // (list size, weight)
        }

        val xAxis = binding.workoutLayoutWeightGraph.xAxis

        val set1: LineDataSet = LineDataSet(values, "DataSet 1")

        val dataSets: ArrayList<ILineDataSet> = ArrayList()
        dataSets.add(set1) // add the data sets

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
            setDrawIcons(false)
//            setDrawValues(false)
//            setDrawFilled(false)
//            setDrawCircleHole(false)
            setDrawHighlightIndicators(false)
//            setDrawHorizontalHighlightIndicator(false)
//            setDrawVerticalHighlightIndicator(false)
        }


        // set data
        binding.workoutLayoutWeightGraph.setData(data)
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
        viewModel.defaultTodayExItemList()
    }

}