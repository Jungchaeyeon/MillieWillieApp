package com.makeus.milliewillie.ui.routine

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import java.util.*


class ExerciseSetViewModel: BaseViewModel() {

    val liveDataRoutineKind = MutableLiveData<String>().apply { value = "옵션 선택" }
    val liveDataAddSetList = MutableLiveData<List<String>>()

    init {
        defaultAddSet()
    }

    fun defaultAddSet() {
        listOf<String>("2 세트", "3 세트")
    }

}