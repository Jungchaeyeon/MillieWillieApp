package com.makeus.milliewillie.ui.dDay

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import java.util.*

class DdayViewModel : BaseViewModel() {

    val liveDataDdayDate = MutableLiveData<String>().apply { value = date() }

    private fun date(): String? {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR).toString() + "년 " + (today.get(Calendar.MONTH) + 1).toString() + "월 "+ today.get(Calendar.DAY_OF_MONTH).toString() + "일"
    }

}