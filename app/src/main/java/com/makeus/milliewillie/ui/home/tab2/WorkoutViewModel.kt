package com.makeus.milliewillie.ui.home.tab2

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.exerciseId
import com.makeus.milliewillie.util.Log
import java.util.*
import kotlin.collections.ArrayList

class WorkoutViewModel(val apiRepository: ApiRepository): BaseViewModel() {
    val liveRecordWeightItemList = MutableLiveData<ArrayList<WorkoutWeightRecordDate>>()
    val recordWeightArrayList = ArrayList<WorkoutWeightRecordDate>()
    var liveRecordWeightItemListSize = recordWeightArrayList.size
//    val todayExArrayList = arrayListOf<TodayExDayOfWeek>(TodayExDayOfWeek("일"),TodayExDayOfWeek("월"),
//        TodayExDayOfWeek("화"), TodayExDayOfWeek("수"), TodayExDayOfWeek("목")
//        ,TodayExDayOfWeek("목"), TodayExDayOfWeek("금"), TodayExDayOfWeek("오늘"))

    val liveRoutineItemList = MutableLiveData<ArrayList<MyRoutineInfo>>()
    val _routineArrayList = ArrayList<MyRoutineInfo>()
//    val _routineArrayList = ArrayList<TodayRoutines>()

    var liveDataToday = MutableLiveData<String>().apply { value = "" }

    var goalWeight = ""
    val goalWeightText = MutableLiveData<String>().apply { value = goalWeight }

    init{
        defaultRecordWeightItemList()
        defaultRoutineItemList()
    }

    fun getDailyWeight() = apiRepository.getDailyWeight(exerciseId)



    fun defaultRecordWeightItemList() {
        liveRecordWeightItemListSize = recordWeightArrayList.size
        liveRecordWeightItemList.postValue(recordWeightArrayList)
        Log.e(liveRecordWeightItemList.value.toString())
    }

    fun createWeightItem(weightItems: ArrayList<DailyWeight>, dateItems: ArrayList<WeightDay>){
        recordWeightArrayList.clear()
        for (i in 0 until weightItems.size) {
            recordWeightArrayList.add(WorkoutWeightRecordDate(weight = weightItems[i].dailyWeight, date = dateItems[i].weightDay))
        }
        Log.e("onBackPressed called")
        Log.e(recordWeightArrayList.toString())
        defaultRecordWeightItemList()
    }

    fun addWeightItem(item: WorkoutWeightRecordDate) {
        if (recordWeightArrayList.size < 5) {
            recordWeightArrayList.add(WorkoutWeightRecordDate(weight = item.weight, date = item.date))

            liveRecordWeightItemList.value = recordWeightArrayList
            liveRecordWeightItemListSize = recordWeightArrayList.size
            android.util.Log.e("makeUs", "checkArrayList: $recordWeightArrayList")
        } else {
            removeWeightItem(0)
            recordWeightArrayList.add(WorkoutWeightRecordDate(weight = item.weight, date = item.date))

            liveRecordWeightItemList.value = recordWeightArrayList
            liveRecordWeightItemListSize = recordWeightArrayList.size
            android.util.Log.e("makeUs", "checkArrayList: $recordWeightArrayList")
        }
    }

    fun removeWeightItem(position: Int) {
        recordWeightArrayList.removeAt(position)
        liveRecordWeightItemList.value = recordWeightArrayList
        liveRecordWeightItemListSize = recordWeightArrayList.size
    }

    fun defaultRoutineItemList() {
        liveRoutineItemList.postValue(_routineArrayList)
    }

    fun createRoutineList(routineList: ArrayList<MyRoutineInfo>): ArrayList<MyRoutineInfo>{
        _routineArrayList.clear()

        for (i in routineList) {
            _routineArrayList.add(
                MyRoutineInfo(
                    routineName =  i.routineName,
                    routineRepeatDay = i.routineRepeatDay,
                    routineId = i.routineId,
                    isDoneRoutine = i.isDoneRoutine
                )
            )
        }
        defaultRoutineItemList()

        return _routineArrayList
    }

//    fun addRoutineItem(item: MyRoutineInfo) {
//        _routineArrayList.add(MyRoutineInfo(
//            routineName = item.routineName, dayOfWeek = item.dayOfWeek)
//        )
//        liveRoutineItemList.value = _routineArrayList
//        android.util.Log.e("makeUs", "_routineArrayList: $_routineArrayList")
//
//    }

    fun removeRoutineItem(position: Int) {
        _routineArrayList.removeAt(position)
        liveRoutineItemList.value = _routineArrayList
        android.util.Log.e("makeUs", "_routineArrayList: $_routineArrayList")
    }

}