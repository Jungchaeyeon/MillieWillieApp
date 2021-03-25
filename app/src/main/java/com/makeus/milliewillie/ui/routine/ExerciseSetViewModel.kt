package com.makeus.milliewillie.ui.routine

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.WorkoutCountSet
import com.makeus.milliewillie.model.WorkoutTimeSet
import com.makeus.milliewillie.model.WorkoutWncSet
import kotlin.collections.ArrayList


class ExerciseSetViewModel: BaseViewModel() {

    val liveDataWncAddSetList = MutableLiveData<ArrayList<WorkoutWncSet>>()
    val liveDataCountAddSetList = MutableLiveData<ArrayList<WorkoutCountSet>>()
    val liveDataTimeAddSetList = MutableLiveData<ArrayList<WorkoutTimeSet>>()

    val wncSetItemList = ArrayList<WorkoutWncSet>()
    val countSetItemList = ArrayList<WorkoutCountSet>()
    val timeSetItemList = ArrayList<WorkoutTimeSet>()
    var wncSetItemListSize = wncSetItemList.size
    var countSetItemListSize = countSetItemList.size
    var timeSetItemListSize = timeSetItemList.size

    val liveSwitchStatus = MutableLiveData<Boolean>()
    var liveDataSetCount = MutableLiveData<String>().apply { value = setCount.toString() }
    var setCount = 0

    var liveDataUnderSetCount = MutableLiveData<String>().apply { value = "0 세트" }

    var liveDataExerciseName = MutableLiveData<String>().apply { value = "" }
    
    init {
        defaultAddSet()
    }

    fun increaseSetCount() {
        setCount += 1
        liveDataSetCount.postValue(setCount.toString())
        liveDataUnderSetCount.postValue("$setCount 세트")
        addItem()
    }
    fun decreaseSetCount() {
        setCount -= 1
        liveDataSetCount.postValue(setCount.toString())
        liveDataUnderSetCount.postValue("$setCount 세트")
        removeItem()
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
        wncSetItemList.add(WorkoutWncSet("$setCount 세트"))
        wncSetItemListSize = wncSetItemList.size
        countSetItemList.add(WorkoutCountSet("$setCount 세트"))
        countSetItemListSize = countSetItemList.size
        timeSetItemList.add(WorkoutTimeSet("$setCount 세트"))
        timeSetItemListSize = timeSetItemList.size

        liveDataWncAddSetList.postValue(wncSetItemList)
        defaultAddSet()
    }

    fun removeItem() {
        //wncSetItemList.removeLast()
        wncSetItemList.removeAt(wncSetItemListSize-1)
        wncSetItemListSize = wncSetItemList.size
        countSetItemList.removeAt(countSetItemListSize-1)
        countSetItemListSize = countSetItemList.size
        timeSetItemList.removeAt(timeSetItemListSize-1)
        timeSetItemListSize = timeSetItemList.size

        defaultAddSet()
    }

    fun addPositionItem(setOption: ExerciseSetBottomSheetFragment.SetOptions, position: Int, value: String, kind: Int) {
        when (setOption) {
            ExerciseSetBottomSheetFragment.SetOptions.WNC-> {
                when (kind) {
                    1 -> wncSetItemList[position].weight = value
                    2 -> wncSetItemList[position].count = value
                }
            }
            ExerciseSetBottomSheetFragment.SetOptions.COUNT-> {
                countSetItemList[position].count = value
            }
            ExerciseSetBottomSheetFragment.SetOptions.TIME-> {
                when (kind) {
                    1 -> timeSetItemList[position].hour = value
                    2 -> timeSetItemList[position].min = value
                    3 -> timeSetItemList[position].sec = value
                }
            }
        }

    }

}