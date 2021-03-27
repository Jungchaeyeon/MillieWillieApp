package com.makeus.milliewillie.ui.holiday

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel

class HoliViewModel : BaseViewModel() {

    var pickableMax =50

    var regularHoliNum = 24
    var prizeHoliNum = 15
    var otherHoliNum = 15

    val liveAlreadyUseDays = MutableLiveData<Int>().apply { value =24 }
    val liveNotUseDays = MutableLiveData<Int>().apply { value =24 }
    val liveRegularHoliday =  MutableLiveData<String>().apply { value = "0일"}
    val liveRegularWholeHoliday = MutableLiveData<String>().apply { value = " /24일"}
    val livePrizeHoliday = MutableLiveData<String>().apply { value = "0일" }
    val livePrizeWholeHoliday = MutableLiveData<String>().apply { value = " /15일" }
    val liveOtherHoliday = MutableLiveData<String>().apply { value = " 0일" }
    val liveOtherWholeHoliday = MutableLiveData<String>().apply { value = "/15일" }

    //activity_holiday_edit
    val liveHoliType = MutableLiveData<String>()


}