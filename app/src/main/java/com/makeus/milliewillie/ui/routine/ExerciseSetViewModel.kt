package com.makeus.milliewillie.ui.routine

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.WorkoutCountSet
import com.makeus.milliewillie.model.WorkoutTimeSet
import com.makeus.milliewillie.model.WorkoutWncSet
import com.makeus.milliewillie.util.Log
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

    var liveDataSetCount = MutableLiveData<String>().apply { value = "0" }
    var liveDataUnderSetCount = MutableLiveData<String>().apply { value = "0세트" }

    var liveDataExerciseName = MutableLiveData<String>().apply { value = "" }
    
    init {
        defaultAddSet()
//        addItem()
    }

    fun increaseSetCount(value: String) {
        liveDataSetCount.postValue(value)
        liveDataUnderSetCount.postValue("${value}세트")

        addItem()

    }
    fun decreaseSetCount(value: String) {
        liveDataSetCount.postValue(value)
        liveDataUnderSetCount.postValue("${value}세트")
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
        //매개변수로 데이터 받아서 +1 하고 if로 조절하면 될 것 같음 1세트 부터 시작하는 거
//        if (liveDataSetCount.value!!.toInt() == 1) {
//            Log.e("liveDataSetCount.value = ${liveDataSetCount.value}")
//            wncSetItemList.add(WorkoutWncSet("${liveDataSetCount.value!!.toInt()}세트"))
//            countSetItemList.add(WorkoutCountSet("${liveDataSetCount.value!!.toInt()}세트"))
//            timeSetItemList.add(WorkoutTimeSet("${liveDataSetCount.value!!.toInt()}세트"))
//        } else {
            Log.e("liveDataSetCount = ${liveDataSetCount.value}")
            wncSetItemList.add(WorkoutWncSet("${liveDataSetCount.value!!.toInt()+1}세트"))
            countSetItemList.add(WorkoutCountSet("${liveDataSetCount.value!!.toInt()+1}세트"))
            timeSetItemList.add(WorkoutTimeSet("${liveDataSetCount.value!!.toInt()+1}세트"))
//        }

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

    fun resetItems() {
        wncSetItemList.clear()
        countSetItemList.clear()
        timeSetItemList.clear()

        for (i in 0 until liveDataSetCount.value!!.toInt()) {
            wncSetItemList.add(WorkoutWncSet("${i+1}세트"))
            countSetItemList.add(WorkoutCountSet("${i+1}세트"))
            timeSetItemList.add(WorkoutTimeSet("${i+1}세트"))
        }

        defaultAddSet()
    }

}