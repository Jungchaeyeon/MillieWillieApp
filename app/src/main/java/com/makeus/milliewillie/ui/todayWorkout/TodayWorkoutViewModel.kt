package com.makeus.milliewillie.ui.todayWorkout

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.TodayRoutines

class TodayWorkoutViewModel: BaseViewModel() {

    var liveDataTextEdit = MutableLiveData<Int>().apply { value = R.string.edit }

    var liveDataToday = MutableLiveData<String>().apply { value = "" }

    val liveRoutineItemList = MutableLiveData<ArrayList<TodayRoutines>>()
    val _routineArrayList = arrayListOf<TodayRoutines>(
        TodayRoutines("어깨깡새우깡", "매일"),
        TodayRoutines("힙깡새우깡", "월, 화"),
        TodayRoutines("1일1깡새우깡", "화, 수, 목")
    )

    init {
        defaultRoutineItemList()
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