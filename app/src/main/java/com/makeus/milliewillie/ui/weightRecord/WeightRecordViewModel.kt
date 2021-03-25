package com.makeus.milliewillie.ui.weightRecord

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.MyApplication.Companion.getString
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.DailyWeight
import com.makeus.milliewillie.model.WeightDay
import com.makeus.milliewillie.model.WeightPerDay
import com.makeus.milliewillie.model.WorkoutWeightRecordDate
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.util.Log
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.collections.ArrayList

class WeightRecordViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    val liveDataWeightRecordList = MutableLiveData<ArrayList<WorkoutWeightRecordDate>>()
    val _weightRecordArrayList = ArrayList<WorkoutWeightRecordDate>()
    var liveDataWeightRecordListSize = _weightRecordArrayList.size

    val liveDataWeightPerDay = MutableLiveData<ArrayList<WeightPerDay>>()
    val _weightPerDayArrayList = ArrayList<WeightPerDay>()

    var yearAndMonth = MutableLiveData<String>().apply {
        value = "${Calendar.getInstance().get(Calendar.YEAR)}년 ${Calendar.getInstance().get(Calendar.MONTH)+1}월"
    }

    var recordGoalWeight = MutableLiveData<String>().apply { value = "0" }
    var topRecordGoalWeight = MutableLiveData<String>().apply { value = recordGoalWeight.value }

    init{
        defaultWeightRecordItemList()
        defaultWeightPerDay(_weightPerDayArrayList)
    }

    // 체중 기록 GET API
    fun getWeightRecord(path: Long, viewMonth: Int, viewYear: Int) = apiRepository.getWeightRecord(path = path, viewMonth = viewMonth, viewYear = viewYear)


    // 월별 체중 기록 리스트
    fun defaultWeightRecordItemList() {
        liveDataWeightRecordListSize = _weightRecordArrayList.size
        liveDataWeightRecordList.postValue(_weightRecordArrayList)
    }

    fun createWeightRecordItem(itemList: ArrayList<WorkoutWeightRecordDate>){
        _weightRecordArrayList.clear()
        for (i in 0 until itemList.size) {
            _weightRecordArrayList.add(0, WorkoutWeightRecordDate(weight = itemList[i].weight, date = itemList[i].date))
            Log.e(_weightRecordArrayList.toString())
        }

        defaultWeightRecordItemList()
    }

    // 해당 월 일자별 체중 기록 리스트
    fun defaultWeightPerDay(dayWeight: ArrayList<WeightPerDay>) {
        _weightPerDayArrayList.clear()

        for (i in 0 until dayWeight.size) {
            _weightPerDayArrayList.add(dayWeight[i])
        }

        liveDataWeightPerDay.postValue(_weightPerDayArrayList)
    }

    fun replaceItem(position: Int, value: String) {
        val dayOfMonth = _weightPerDayArrayList[position].dayOfMonth
        val PMAmount = _weightPerDayArrayList[position].PMAmount
        _weightPerDayArrayList.removeAt(position)
        _weightPerDayArrayList.add(position, WeightPerDay(dayOfMonth = dayOfMonth, currentWeight = value, PMAmount = PMAmount))

        liveDataWeightPerDay.postValue(_weightPerDayArrayList)
    }

//    fun getDaysInMonth(month: Int, year: Int): Int{
//        return when (month - 1) {
//            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
//            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
//            Calendar.FEBRUARY -> if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) 29 else 28
//            else -> throw IllegalArgumentException("Invalid Month")
//        }
//    }

}