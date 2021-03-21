package com.makeus.milliewillie.ui.weightRecord

import android.annotation.SuppressLint
import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.model.WeightPerDay
import com.makeus.milliewillie.ui.fragment.DatePickerDdayBottomSheetDialogFragment
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class WeightRecordActivity :
    BaseDataBindingActivity<ActivityWeightRecordBinding>(R.layout.activity_weight_record) {

    private val viewModel by viewModel<WeightRecordViewModel>()

    private var goalValue: Float = 0f
    private var currentValue: Float = 0f

    val calendar = Calendar.getInstance()

    val todayMonth = calendar.get(Calendar.MONTH)
    val today = calendar.get(Calendar.DAY_OF_MONTH)

    @SuppressLint("ResourceAsColor", "StringFormatMatches")
    override fun ActivityWeightRecordBinding.onBind() {
        vi = this@WeightRecordActivity
        vm = viewModel

        //라인차트 함수 호출
        setLineChart()

        binding.weightRecordRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<WeightPerDay>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WeightPerDay, ActivityWeightRecordRecyclerItemBinding>(R.layout.activity_weight_record_recycler_item) {
                        vi = this@WeightRecordActivity
                        item = it
                    }
                )
        }


    }

    // 일자 없앤 데이트피커로 새로 만들어서 변경 예정
    fun onClickSetDate() {
        DatePickerDdayBottomSheetDialogFragment.getInstance()
            .setOnClickOk {date, gapDay, year, month ->
                viewModel.yearAndMonth.postValue("${year}년 ${month}월")
                viewModel.replaceWeightPerDay(month = month.toInt())
//                viewModel.liveDataWeightPerDay.observe(this, androidx.lifecycle.Observer { viewModel.defaultWeightPerDay() })
            }.show(supportFragmentManager)
    }

    fun setLineChart() {
        val values = ArrayList<Entry>()
        val goalWeight = ArrayList<Entry>()

        for(i in 0 until viewModel.liveDataWeightRecordListSize){
            Log.e("${viewModel.liveDataWeightRecordList.value!![i].weight.toInt().toFloat()}")
            values.add(Entry (i.toFloat(), viewModel.liveDataWeightRecordList.value!![i].weight.toInt().toFloat())) // (x값, y값) // (list size, weight)
        }

        goalWeight.add(Entry(0f, goalValue))
        goalWeight.add(Entry(5f, goalValue))

        val xAxis = binding.weightRecordLineChart.xAxis

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

        binding.weightRecordLineChart.apply {
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
        binding.weightRecordLineChart.setData(data)
        binding.weightRecordLineChart.notifyDataSetChanged()
        binding.weightRecordLineChart.invalidate()
    }

    override fun onResume() {
        super.onResume()
        viewModel.defaultWeightRecordItemList()
    }

}