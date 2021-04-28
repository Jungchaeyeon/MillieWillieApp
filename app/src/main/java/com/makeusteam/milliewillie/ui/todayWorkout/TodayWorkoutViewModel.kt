package com.makeusteam.milliewillie.ui.todayWorkout

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.model.MyRoutineInfo
import com.makeusteam.milliewillie.repository.ApiRepository

class TodayWorkoutViewModel(val apiRepository: ApiRepository): BaseViewModel() {

    var liveDataTextEdit = MutableLiveData<Int>().apply { value = R.string.edit }

    var liveDataToday = MutableLiveData<String>().apply { value = "" }

    val liveRoutineItemList = MutableLiveData<ArrayList<MyRoutineInfo>>()
    val _calRoutineArrayList = ArrayList<MyRoutineInfo>()

    init {
        defaultRoutineItemList()
    }


    fun defaultRoutineItemList() {
        liveRoutineItemList.postValue(_calRoutineArrayList)
        android.util.Log.e("makeUs", "liveRoutineItemList: ${liveRoutineItemList.value}")
    }

    fun createRoutineItem(item: ArrayList<MyRoutineInfo>): ArrayList<MyRoutineInfo> {
        _calRoutineArrayList.clear()

        for (i in item) _calRoutineArrayList.add(MyRoutineInfo(routineName = i.routineName, routineRepeatDay = i.routineRepeatDay, routineId = i.routineId, isDoneRoutine = i.isDoneRoutine))

        defaultRoutineItemList()

        return _calRoutineArrayList
    }

    fun removeRoutineItem(position: Int) {
        _calRoutineArrayList.removeAt(position)
        defaultRoutineItemList()
        android.util.Log.e("makeUs", "_routineArrayList: $_calRoutineArrayList")
    }

}