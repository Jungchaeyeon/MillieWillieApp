package com.makeusteam.milliewillie.ui.weightRecord

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.MyApplication.Companion.globalApplicationContext
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.*
import com.makeusteam.milliewillie.model.*
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.SampleToast
import com.makeusteam.milliewillie.ui.fragment.DatePickekWeightRecortBottomSheetDialogFragment
import com.makeusteam.milliewillie.ui.home.tab2.WeightAddRecordBottomSheetFragment
import com.makeusteam.milliewillie.util.ConvertTimeMills
import com.makeusteam.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class WeightRecordActivity :
    BaseDataBindingActivity<ActivityWeightRecordBinding>(R.layout.activity_weight_record) {

    private val viewModel by viewModel<WeightRecordViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    private var goalValue: Float = 0f
    private var currentValue: Float = 0f

    val calendar = Calendar.getInstance()

    private var recordDatePickerYear: Int = calendar.get(Calendar.YEAR)
    private var recordDatePickerMonth: Int = calendar.get(Calendar.MONTH) + 1

    var selectYear = calendar.get(Calendar.YEAR)

    val todayYear = calendar.get(Calendar.YEAR)
    val todayMonth = calendar.get(Calendar.MONTH) + 1
    val today = calendar.get(Calendar.DAY_OF_MONTH)

    var position by Delegates.notNull<Int>()

    private val monthWeightArray = ArrayList<WorkoutWeightRecordDate>()
    private val dayWeightArray = ArrayList<WeightPerDay>()

    override fun onResume() {
        super.onResume()

        setLineChart()
    }

    @SuppressLint("ResourceAsColor", "StringFormatMatches")
    override fun ActivityWeightRecordBinding.onBind() {
        vi = this@WeightRecordActivity
        vm = viewModel

        executeGetWeightRecord(month = todayMonth, year = todayYear)

        binding.weightRecordWeightRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutWeightRecordDate>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutWeightRecordDate, WeightRecordRecyclerItemBinding>(R.layout.weight_record_recycler_item) {
                        vi = this@WeightRecordActivity
                        item = it
                    }
                )
        }

        binding.weightRecordRecycler.run {
            // ????????? ?????? ?????????
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    val position = child?.let { rv.getChildAdapterPosition(it) }
                    if (e.action == MotionEvent.ACTION_MOVE) return false
                    else if (e.action == MotionEvent.ACTION_UP) {
                        Log.e("$position")
                        if (position != null) {
                            this@WeightRecordActivity.position = position
                            return false
                        }
                        return true
                    }
                    return false
                }
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })

            adapter = BaseDataBindingRecyclerViewAdapter<WeightPerDay>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WeightPerDay, ActivityWeightRecordRecyclerItemBinding>(R.layout.activity_weight_record_recycler_item) {
                        vi = this@WeightRecordActivity
                        item = it
                    }
                )
        }

    }

    fun todayWeightInput() {
        val calendarInstance = Calendar.getInstance()
        val date = viewModel.liveDataWeightPerDay.value?.get(position)?.dayOfMonth
        var dayText = ""
        var index = 0
        for (i in date!!.indices) {
            if (date[i] == '???') index = i + 1
            if (date[index] == '???') break
            if (index > 0) {
                dayText += date[index]
                index++
            }
        }


        val selectDateMillis = ConvertTimeMills.ConvertDateToMillis(recordDatePickerYear, recordDatePickerMonth, dayText.toInt())
        val todayMillis = ConvertTimeMills.ConvertDateToMillis(calendarInstance.get(Calendar.YEAR), calendarInstance.get(Calendar.MONTH) + 1, calendarInstance.get(Calendar.DAY_OF_MONTH))

        if (todayMillis < selectDateMillis){
            SampleToast.createToast(this, globalApplicationContext.getString(R.string.over_value_date))?.show()
        } else {
            WeightAddRecordBottomSheetFragment.getInstance()
                .setOnClickOk {
                    val dayWeight = it
                    val year = selectYear
                    var month = ""
                    var day = ""
                    var idx = 0
                    for (i in date!!.indices) {
                        if (date[i] == '???') {
                            for (j in 0 until i) month += date[j].toString()
                            if (month.length < 2) month = "0$month"
                            idx = i + 1
                        } else if (date[i] == '???') {
                            for (j in idx until i) day += date[j].toString()
                            if (day.length < 2) day = "0$day"
                        }

                    }
                    val dateForm = "$year-$month-$day"

                    viewModel.apiRepository.patchTodayWeight(
                        path = repositoryCached.getExerciseId(),
                        body = PatchTodayWeightRequest(
                            dayWeight = it.toDouble(),
                            dayDate = dateForm
                        )
                    )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { it2 ->
                            Log.e(it2.isSuccess.toString())
                            if (it2.isSuccess) {
                                Log.e("?????? ??????")
                                repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, true)

                                viewModel.replaceItem(position, dayWeight)
                                executeGetWeightRecord(month = month.toInt(), year = year.toInt())
                            } else {
                                Log.e("?????? ??????")
                                Log.e(it2.message)
                            }
                        }.disposeOnDestroy(this)
                }.show(supportFragmentManager)
        }

    }

    private fun executeGetWeightRecord(month: Int, year: Int) {
        Log.e("month = $month")
        Log.e("year = $year")
        monthWeightArray.clear()
        dayWeightArray.clear()
        viewModel.getWeightRecord(repositoryCached.getExerciseId(), viewMonth = month, viewYear = year)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    goalValue = it.result.goalWeight.toFloat()
                    if (goalValue == -1.0f) {
                        viewModel.recordGoalWeight.postValue("0")
                        val goalText = String.format(globalApplicationContext.getString(R.string.goal_weight_var, "0"))
                        viewModel.topRecordGoalWeight.postValue(goalText)
                    }
                    else {
                        viewModel.recordGoalWeight.postValue(goalValue.toString())
                        val goalText = String.format(globalApplicationContext.getString(R.string.goal_weight_var, goalValue.toString()))
                        viewModel.topRecordGoalWeight.postValue(goalText)
                    }


                    for (i in 0 until it.result.monthWeight.size()) { // ?????? ?????? ??????
                        monthWeightArray.add(WorkoutWeightRecordDate(
                            weight = it.result.monthWeight[i].asString,
                            date = it.result.monthWeightMonth[i].asString)
                        )
                    }

                    for (i in 0 until it.result.dayWeightDay.size()){ // ????????? ?????? ??? ??????
                        dayWeightArray.add(
                            WeightPerDay(
                                dayOfMonth = it.result.dayWeightDay[i].asString,
                                currentWeight = it.result.dayWeight[i].asString,
                                PMAmount = it.result.dayDif[i].asString
                            )
                        )
                    }
                    viewModel.createWeightRecordItem(monthWeightArray)
                    viewModel.defaultWeightPerDay(dayWeightArray)

                    //???????????? ?????? ??????
                    Handler().postDelayed ({
                        setLineChart()
                    },200)
                } else {
                    Log.e("getWeightRecord ?????? ??????")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this)
    }

    fun modifyGoalWeight() {
        WeightAddRecordBottomSheetFragment.getInstance()
            .setOnClickOk { weight ->
                viewModel.apiRepository.patchGoalWeight(body = PatchGoalWeightRequest(goalWeight = weight.toDouble()),
                    path = repositoryCached.getExerciseId())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Log.e(it.isSuccess.toString())
                        if (it.isSuccess) {
                            Log.e("?????? ??????")

                            viewModel.recordGoalWeight.postValue(weight.toDouble().toString())
                            val goalText = String.format(globalApplicationContext.getString(R.string.goal_weight_var, weight.toDouble().toString()))
                            viewModel.topRecordGoalWeight.postValue(goalText)

                            Handler().postDelayed({
                                setLineChart()
                            }, 200)
                        } else {
                            Log.e("?????? ??????")
                            Log.e(it.message)
                        }
                    }.disposeOnDestroy(this)
            }.show(supportFragmentManager)
    }

    fun onClickSetDate() {
        DatePickekWeightRecortBottomSheetDialogFragment.getInstance()
            .setOnClickOk {year, month ->
                recordDatePickerYear = year.toInt()
                recordDatePickerMonth = month.toInt()
                Log.e("year Calendar.YEAR = $year ${Calendar.YEAR}")
                Log.e("month Calendar.MONTH = $month ${Calendar.MONTH + 1}")
                if (year.toInt() <= Calendar.getInstance().get(Calendar.YEAR)) {
                    if (month.toInt() <= Calendar.getInstance().get(Calendar.MONTH) + 1) {
                        selectYear = year.toInt()
                        viewModel.yearAndMonth.postValue("${year}??? ${month}???")
                        executeGetWeightRecord(month = month.toInt(), year = year.toInt())
                    } else SampleToast.createToast(this, globalApplicationContext.getString(R.string.over_value_month))?.show()
                } else SampleToast.createToast(this, globalApplicationContext.getString(R.string.over_value_month))?.show()
            }.show(supportFragmentManager)
    }

    // ???????????? ??????
    private fun setLineChart() {
        val values = ArrayList<Entry>()
        val goalWeight = ArrayList<Entry>()

        for(i in 0 until viewModel.liveDataWeightRecordListSize){
            Log.e("${viewModel.liveDataWeightRecordList.value!![i].weight.toFloat()}")
            values.add(Entry (i.toFloat(), viewModel.liveDataWeightRecordList.value!![i].weight.toFloat())) // (x???, y???) // (list size, weight)
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
            setDrawGridLines(false) // ??????????????? ????????? ??????
            setDrawAxisLine(false) // ??????????????? ????????? ??????
            setDrawLabels(false) // ?????? ?????? ??????
        }

        binding.weightRecordLineChart.apply {
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

        set1.apply {
            color = Color.parseColor("#00D8FF")
            setCircleColor(Color.parseColor("#00D8FF"))
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

    fun onClickCancel() {
        onBackPressed()
    }


}