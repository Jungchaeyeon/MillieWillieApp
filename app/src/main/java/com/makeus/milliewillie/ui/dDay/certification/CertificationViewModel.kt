package com.makeus.milliewillie.ui.dDay.certification

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import java.util.*

class CertificationViewModel : BaseViewModel() {
    val liveDataToday = MutableLiveData<String>().apply { value = date() }
    val liveDataTodayInfo = MutableLiveData<String>().apply { value = "시작일" }

    private fun date(): String? {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR).toString() + "년 " + (today.get(Calendar.MONTH) + 1).toString() + "월 "+ today.get(
            Calendar.DAY_OF_MONTH).toString() + "일"
    }

}