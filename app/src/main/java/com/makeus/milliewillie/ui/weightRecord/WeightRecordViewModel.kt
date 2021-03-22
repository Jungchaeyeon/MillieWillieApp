package com.makeus.milliewillie.ui.weightRecord

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.MyApplication.Companion.getString
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.WeightPerDay
import com.makeus.milliewillie.model.WorkoutWeightRecordDate
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

class WeightRecordViewModel: BaseViewModel() {

    val liveDataWeightRecordList = MutableLiveData<ArrayList<WorkoutWeightRecordDate>>()
    val _weightRecordArrayList = ArrayList<WorkoutWeightRecordDate>()
    var liveDataWeightRecordListSize = _weightRecordArrayList.size

    val liveDataWeightPerDay = MutableLiveData<ArrayList<WeightPerDay>>()
    val _weightPerDayArrayList = ArrayList<WeightPerDay>()
    var liveDataWeightPerDaySize = getDaysInMonth(Calendar.getInstance().get(Calendar.MONTH) + 1, Calendar.getInstance().get(Calendar.YEAR))

    var yearAndMonth = MutableLiveData<String>().apply {
        value = "${Calendar.getInstance().get(Calendar.YEAR)}년 ${Calendar.getInstance().get(Calendar.MONTH)+1}월"
    }

    init{
        defaultWeightRecordItemList()
        defaultWeightPerDay()
    }

    fun defaultWeightRecordItemList() {
        liveDataWeightRecordList.postValue(_weightRecordArrayList)
    }

    fun defaultWeightPerDay(month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
                            weight: String = getString(R.string.no_data_info),
                            PMAmount: String = getString(R.string.middle_dot)) {
        for (i in 0 until liveDataWeightPerDaySize) {
            _weightPerDayArrayList.add(WeightPerDay(dayOfMonth = "${month}월 ${i+1}일", currentWeight = weight, PMAmount = PMAmount))
        }

        liveDataWeightPerDay.postValue(_weightPerDayArrayList)
    }

    fun replaceWeightPerDay(month: Int = Calendar.getInstance().get(Calendar.MONTH) + 1,
                            weight: String = getString(R.string.no_data_info),
                            PMAmount: String = getString(R.string.middle_dot)) {
        _weightPerDayArrayList.clear()
        liveDataWeightPerDaySize = getDaysInMonth(month,Calendar.getInstance().get(Calendar.YEAR))
        for (i in 0 until liveDataWeightPerDaySize) {
            _weightPerDayArrayList.add(WeightPerDay(dayOfMonth = "${month}월 ${i+1}일", currentWeight = weight, PMAmount = PMAmount))
        }

        liveDataWeightPerDay.postValue(_weightPerDayArrayList)
    }

    fun getDaysInMonth(month: Int, year: Int): Int{
        return when (month - 1) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) 29 else 28
            else -> throw IllegalArgumentException("Invalid Month")
        }
    }

}