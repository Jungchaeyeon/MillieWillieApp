package com.makeus.milliewillie.ui.home.tab2

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.TodayRoutines
import com.makeus.milliewillie.model.WorkoutWeightRecordDate

class WorkoutViewModel: BaseViewModel() {
    val liveRecordWeightItemList = MutableLiveData<ArrayList<WorkoutWeightRecordDate>>()
    val recordWeightArrayList = ArrayList<WorkoutWeightRecordDate>()
    var liveRecordWeightItemListSize = recordWeightArrayList.size
//    val todayExArrayList = arrayListOf<TodayExDayOfWeek>(TodayExDayOfWeek("일"),TodayExDayOfWeek("월"),
//        TodayExDayOfWeek("화"), TodayExDayOfWeek("수"), TodayExDayOfWeek("목")
//        ,TodayExDayOfWeek("목"), TodayExDayOfWeek("금"), TodayExDayOfWeek("오늘"))

    val liveRoutineItemList = MutableLiveData<ArrayList<TodayRoutines>>()
    val _routineArrayList = arrayListOf<TodayRoutines>(
        TodayRoutines("어깨깡새우깡", "매일"),
        TodayRoutines("힙깡새우깡", "월, 화"),
        TodayRoutines("1일1깡새우깡", "화, 수, 목")
    )
//    val _routineArrayList = ArrayList<TodayRoutines>()

    var goalWeight = ""
    val goalWeightText = MutableLiveData<String>().apply { value = goalWeight }

    init{
        defaultRecordWeightItemList()
        defaultRoutineItemList()
    }
    fun defaultRecordWeightItemList() {
        liveRecordWeightItemList.postValue(recordWeightArrayList)
    }

    fun addWeightItem(item: WorkoutWeightRecordDate) {
        if (recordWeightArrayList.size < 5) {
            recordWeightArrayList.add(WorkoutWeightRecordDate(
                weight = item.weight, date = item.date)
            )
            liveRecordWeightItemList.value = recordWeightArrayList
            liveRecordWeightItemListSize = recordWeightArrayList.size
            android.util.Log.e("makeUs", "checkArrayList: $recordWeightArrayList")
        } else {
            removeWeightItem(0)
            recordWeightArrayList.add(WorkoutWeightRecordDate(
                weight = item.weight, date = item.date)
            )
            liveRecordWeightItemList.value = recordWeightArrayList
            liveRecordWeightItemListSize = recordWeightArrayList.size
            android.util.Log.e("makeUs", "checkArrayList: $recordWeightArrayList")
        }
    }

    fun removeWeightItem(position: Int) {
        recordWeightArrayList.removeAt(position)
        liveRecordWeightItemList.value = recordWeightArrayList
        liveRecordWeightItemListSize = recordWeightArrayList.size
        android.util.Log.e("makeUs", "checkArrayList: $recordWeightArrayList")
    }

    fun defaultRoutineItemList() {
        liveRoutineItemList.postValue(_routineArrayList)
    }

    fun addRoutineItem(item: TodayRoutines) {
        _routineArrayList.add(TodayRoutines(
            routineName = item.routineName, dayOfWeek = item.dayOfWeek)
        )
        liveRoutineItemList.value = _routineArrayList
        android.util.Log.e("makeUs", "_routineArrayList: $_routineArrayList")

    }

    fun removeRoutineItem(position: Int) {
        _routineArrayList.removeAt(position)
        liveRoutineItemList.value = _routineArrayList
        android.util.Log.e("makeUs", "_routineArrayList: $_routineArrayList")
    }

}