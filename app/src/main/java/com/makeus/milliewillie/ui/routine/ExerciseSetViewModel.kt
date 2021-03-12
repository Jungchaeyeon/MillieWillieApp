package com.makeus.milliewillie.ui.routine

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import java.util.*


class ExerciseSetViewModel: BaseViewModel() {

    val liveDataRoutineKind = MutableLiveData<String>().apply { value = "옵션 선택" }


}