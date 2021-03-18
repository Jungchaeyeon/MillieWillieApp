package com.makeus.milliewillie.ui.routine

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.WorkoutSet
import kotlin.collections.ArrayList


class ExerciseSetViewModel: BaseViewModel() {

    val liveDataRoutineKind = MutableLiveData<String>().apply { value = "옵션 선택" }
    val liveDataWncAddSetList = MutableLiveData<ArrayList<WorkoutSet>>()
    val liveDataCountAddSetList = MutableLiveData<ArrayList<WorkoutSet>>()
    val liveDataTimeAddSetList = MutableLiveData<ArrayList<WorkoutSet>>()

    val wncSetItemList = arrayListOf<WorkoutSet>(WorkoutSet("2 세트"), WorkoutSet("3 세트"))
    val countSetItemList = arrayListOf<WorkoutSet>(WorkoutSet("2 세트"), WorkoutSet("3 세트"))
    val timeSetItemList = arrayListOf<WorkoutSet>(WorkoutSet("2 세트"), WorkoutSet("3 세트"))
    var wncSetItemListSize = wncSetItemList.size
    var countSetItemListSize = countSetItemList.size
    var timeSetItemListSize = timeSetItemList.size

    init {
        defaultAddSet()
        defaultCountAddSet()
        defaultTimeAddSet()
    }

    fun defaultAddSet() {
        liveDataWncAddSetList.postValue(wncSetItemList)
        wncSetItemListSize = wncSetItemList.size
        timeSetItemListSize = wncSetItemList.size
    }

    fun defaultCountAddSet() {
        liveDataCountAddSetList.postValue(countSetItemList)
        countSetItemListSize = countSetItemList.size
    }

    fun defaultTimeAddSet() {
        liveDataTimeAddSetList.postValue(timeSetItemList)
        timeSetItemListSize = timeSetItemList.size
    }

    fun addItem(view: View) {
        when (view.id) {
            R.id.rebs_wnc_layout_add -> {
                wncSetItemList.add(WorkoutSet("${wncSetItemListSize + 2} 세트"))
                wncSetItemListSize = wncSetItemList.size
                android.util.Log.e("makeUs", "wncSetItemList: $wncSetItemList")
            }
            R.id.rebs_count_layout_add -> {
                countSetItemList.add(WorkoutSet("${countSetItemListSize + 2} 세트"))
                countSetItemListSize = countSetItemList.size
                android.util.Log.e("makeUs", "countSetItemList: $countSetItemList")
            }
            R.id.rebs_time_layout_add -> {
                timeSetItemList.add(WorkoutSet("${timeSetItemListSize + 2} 세트"))
                timeSetItemListSize = timeSetItemList.size
                android.util.Log.e("makeUs", "timeSetItemList: $timeSetItemList")
            }
        }

    }

    fun removeItem(position: Int) {
        wncSetItemList.removeAt(position)
        wncSetItemListSize = wncSetItemList.size
    }

}