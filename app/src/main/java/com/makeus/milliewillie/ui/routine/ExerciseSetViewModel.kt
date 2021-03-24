package com.makeus.milliewillie.ui.routine

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.WorkoutSet
import kotlin.collections.ArrayList


class ExerciseSetViewModel: BaseViewModel() {

    val liveDataWncAddSetList = MutableLiveData<ArrayList<WorkoutSet>>()
    val liveDataCountAddSetList = MutableLiveData<ArrayList<WorkoutSet>>()
    val liveDataTimeAddSetList = MutableLiveData<ArrayList<WorkoutSet>>()

    val wncSetItemList = ArrayList<WorkoutSet>()
    val countSetItemList = ArrayList<WorkoutSet>()
    val timeSetItemList = ArrayList<WorkoutSet>()
    var wncSetItemListSize = wncSetItemList.size
    var countSetItemListSize = countSetItemList.size
    var timeSetItemListSize = timeSetItemList.size

    var liveDataSetCount = MutableLiveData<String>().apply { value = "0" }
    var liveDataUnderSetCount = MutableLiveData<String>().apply { value = "0 세트" }

    init {
        defaultAddSet()
    }

    fun increaseSetCount(value: String) {
        liveDataSetCount.postValue(value)
        liveDataUnderSetCount.postValue("$value 세트")
    }
    fun decreaseSetCount(value: String) {
        liveDataSetCount.postValue(value)
        liveDataUnderSetCount.postValue("$value 세트")
    }

    fun defaultAddSet() {
        wncSetItemListSize = wncSetItemList.size
        liveDataWncAddSetList.postValue(wncSetItemList)
        countSetItemListSize = countSetItemList.size
        liveDataCountAddSetList.postValue(countSetItemList)
        timeSetItemListSize = timeSetItemList.size
        liveDataTimeAddSetList.postValue(timeSetItemList)
    }

    fun addItem() {
        wncSetItemList.add(WorkoutSet("${liveDataSetCount.value!!.toInt()+1} 세트"))
        wncSetItemListSize = wncSetItemList.size
        countSetItemList.add(WorkoutSet("${liveDataSetCount.value!!.toInt()+1} 세트"))
        countSetItemListSize = countSetItemList.size
        timeSetItemList.add(WorkoutSet("${liveDataSetCount.value!!.toInt()+1} 세트"))
        timeSetItemListSize = timeSetItemList.size

        defaultAddSet()
    }

    fun removeItem() {
        wncSetItemList.removeAt(wncSetItemListSize-1)
        wncSetItemListSize = wncSetItemList.size
        countSetItemList.removeAt(countSetItemListSize-1)
        countSetItemListSize = countSetItemList.size
        timeSetItemList.removeAt(timeSetItemListSize-1)
        timeSetItemListSize = timeSetItemList.size

        defaultAddSet()
    }

}