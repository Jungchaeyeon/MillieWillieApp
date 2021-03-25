package com.makeus.milliewillie.ui.todayWorkout

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.MyRoutineInfo
import com.makeus.milliewillie.model.TodayRoutines
import com.makeus.milliewillie.repository.ApiRepository

class TodayWorkoutViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    var liveDataTextEdit = MutableLiveData<Int>().apply { value = R.string.edit }

    var liveDataToday = MutableLiveData<String>().apply { value = "" }

    val liveRoutineItemList = MutableLiveData<ArrayList<MyRoutineInfo>>()
    val _routineArrayList = ArrayList<MyRoutineInfo>()

    init {
        defaultRoutineItemList()
    }


    fun defaultRoutineItemList() {
        liveRoutineItemList.postValue(_routineArrayList)
    }

    fun createRoutineItem(item: ArrayList<MyRoutineInfo>) {
        _routineArrayList.clear()

        for (i in item) _routineArrayList.add(MyRoutineInfo(routineName = i.routineName, routineRepeatDay = i.routineRepeatDay, routineId = i.routineId))

        defaultRoutineItemList()
        android.util.Log.e("makeUs", "_routineArrayList: $_routineArrayList")

    }

    fun removeRoutineItem(position: Int) {
        _routineArrayList.removeAt(position)
        defaultRoutineItemList()
        android.util.Log.e("makeUs", "_routineArrayList: $_routineArrayList")
    }

}