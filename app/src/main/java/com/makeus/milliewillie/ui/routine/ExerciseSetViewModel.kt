package com.makeus.milliewillie.ui.routine

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import java.util.*
import kotlin.collections.ArrayList


class ExerciseSetViewModel: BaseViewModel() {

    val liveDataRoutineKind = MutableLiveData<String>().apply { value = "옵션 선택" }
    val liveDataAddSetList = MutableLiveData<ArrayList<String>>()

    val setItemList = arrayListOf<String>("2 세트", "3 세트")
    var liveDataSetItemListSize = setItemList.size

    init {
        defaultAddSet()
    }

    fun defaultAddSet() {
        liveDataAddSetList.postValue(setItemList)
        liveDataSetItemListSize = setItemList.size
    }

    fun addItem() {
        liveDataSetItemListSize = setItemList.size
        setItemList.add("${liveDataSetItemListSize + 1} 세트")
    }

    fun removeItem(position: Int) {
        setItemList.removeAt(position)
        liveDataSetItemListSize = setItemList.size
    }

}