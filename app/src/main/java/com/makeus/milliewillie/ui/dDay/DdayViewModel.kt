package com.makeus.milliewillie.ui.dDay

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import java.util.*

class DdayViewModel : BaseViewModel() {

    val liveDataDdayDate = MutableLiveData<String>().apply { value = " " }

    val liveDataDayGap = MutableLiveData<String>().apply { value = "날짜선택"}

    private fun date(): String? {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR).toString() + "년 " + (today.get(Calendar.MONTH) + 1).toString() + "월 "+ today.get(Calendar.DAY_OF_MONTH).toString() + "일"
    }

}