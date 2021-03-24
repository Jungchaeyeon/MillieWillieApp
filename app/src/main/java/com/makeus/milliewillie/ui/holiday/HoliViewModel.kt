package com.makeus.milliewillie.ui.holiday

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.HolidayItem
import com.makeus.milliewillie.util.Log

class HoliViewModel : BaseViewModel() {

    val liveHoliItems = MutableLiveData<ArrayList<HolidayItem>>()
    var holiItems = ArrayList<HolidayItem>()
    var pickableMax =8

    val livePrizeHoliday = MutableLiveData<String>().apply { value = "0일" }
    val liveRegularHoliday =List(3) { MutableLiveData<String>().apply { value = "0일"} }
    val liveRegularWholeHoliday =List(3) { MutableLiveData<String>().apply { value = " /8일"} }
    var holidayItem : HolidayItem = HolidayItem()

    init {
    }

    //HolidayMethod
    fun addHoli(item: HolidayItem) {
        holiItems.add(item)
        liveHoliItems.value = holiItems
    }

    fun removeHoli(item: HolidayItem) {
        holiItems.remove(item)
        liveHoliItems.value = holiItems
    }



}