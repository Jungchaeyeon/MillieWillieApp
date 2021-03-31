package com.makeus.milliewillie.ui.weightRecord

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
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.MyApplication.Companion.exerciseId
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.ui.fragment.DatePickekWeightRecortBottomSheetDialogFragment
import com.makeus.milliewillie.ui.home.tab2.WeightAddRecordBottomSheetFragment
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
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
            // 아이템 클릭 리스너
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

    @RequiresApi(Build.VERSION_CODES.O)
    fun todayWeightInput() {
        if (!repositoryCached.getIsInputWeight()) {
            val date = viewModel.liveDataWeightPerDay.value?.get(position)?.dayOfMonth
            var dayText = ""
            var index = 0
            for (i in date!!.indices) {
                if (date[i] == '월') index = i + 1
                if (date[index] == '일') break
                if (index > 0) {
                    dayText += date[index]
                    index++
                }
            }

            if (dayText.toInt() > Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) {
                getString(R.string.over_value_date).showShortToastSafe()
            } else {
                WeightAddRecordBottomSheetFragment.getInstance()
                    .setOnClickOk {
                        val dayWeight = it
                        val year = Calendar.getInstance().get(Calendar.YEAR)
                        var month = ""
                        var day = ""
                        var idx = 0
                        for (i in date!!.indices) {
                            if (date[i] == '월') {
                                for (j in 0 until i) month += date[j].toString()
                                if (month.length < 2) month = "0$month"
                                idx = i + 1
                            } else if (date[i] == '일') {
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
                                    Log.e("호출 성공")
                                    repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, true)

                                    viewModel.replaceItem(position, dayWeight)
                                    executeGetWeightRecord(month = todayMonth, year = todayYear)
                                } else {
                                    Log.e("호출 실패")
                                    Log.e(it2.message)
                                }
                            }.disposeOnDestroy(this)
                    }.show(supportFragmentManager)
            }
        }

    }

    private fun executeGetWeightRecord(month: Int, year: Int) {
        monthWeightArray.clear()
        dayWeightArray.clear()
        viewModel.getWeightRecord(exerciseId, viewMonth = month, viewYear = year)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    goalValue = it.result.goalWeight.toFloat()
                    viewModel.recordGoalWeight.postValue(goalValue.toString())
                    val goalText = String.format(getString(R.string.goal_weight_var, goalValue.toString()))
                    viewModel.topRecordGoalWeight.postValue(goalText)

                    for (i in 0 until it.result.monthWeight.size()) { // 월별 체중 평균
                        monthWeightArray.add(WorkoutWeightRecordDate(
                            weight = it.result.monthWeight[i].asString,
                            date = it.result.monthWeightMonth[i].asString)
                        )
                    }

                    for (i in 0 until it.result.dayWeightDay.size()){ // 일자별 해당 월 체중
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

                    //라인차트 함수 호출
                    Handler().postDelayed ({
                        setLineChart()
                    },200)
                } else {
                    Log.e("getWeightRecord 호출 실패")
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
                            Log.e("호출 성공")

                            viewModel.recordGoalWeight.postValue(weight.toDouble().toString())
                            val goalText = String.format(getString(R.string.goal_weight_var, weight.toDouble().toString()))
                            viewModel.topRecordGoalWeight.postValue(goalText)

                            Handler().postDelayed({
                                setLineChart()
                            }, 200)
                        } else {
                            Log.e("호출 실패")
                            Log.e(it.message)
                        }
                    }.disposeOnDestroy(this)
            }.show(supportFragmentManager)
    }

    fun onClickSetDate() {
        DatePickekWeightRecortBottomSheetDialogFragment.getInstance()
            .setOnClickOk {year, month ->
                if (year.toInt() <= Calendar.getInstance().get(Calendar.YEAR)) {
                    if (month.toInt() <= Calendar.getInstance().get(Calendar.MONTH) + 1) {
                        viewModel.yearAndMonth.postValue("${year}년 ${month}월")
                        executeGetWeightRecord(month = month.toInt(), year = year.toInt())
                    } else SampleToast.createToast(this, getString(R.string.over_value_month))?.show()
                } else SampleToast.createToast(this, getString(R.string.over_value_month))?.show()
            }.show(supportFragmentManager)
    }

    // 라인차트 세팅
    private fun setLineChart() {
        val values = ArrayList<Entry>()
        val goalWeight = ArrayList<Entry>()

        for(i in 0 until viewModel.liveDataWeightRecordListSize){
            Log.e("${viewModel.liveDataWeightRecordList.value!![i].weight.toFloat()}")
            values.add(Entry (i.toFloat(), viewModel.liveDataWeightRecordList.value!![i].weight.toFloat())) // (x값, y값) // (list size, weight)
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