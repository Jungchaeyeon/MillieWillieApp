package com.makeusteam.milliewillie.ui.dDay.certification

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.model.DdayCheckList
import java.util.*

class CertificationViewModel : BaseViewModel() {
    val liveDataToday = MutableLiveData<String>().apply { value = date() }
    val liveDataTodayInfo = MutableLiveData<String>().apply { value = "시작일" }

    val certiCheckItemList = MutableLiveData<ArrayList<DdayCheckList>>()

    val certiCheckArrayList = ArrayList<DdayCheckList>()

    init{
        defaultCheckItemList()
    }

    fun defaultCheckItemList() {
        certiCheckItemList.postValue(certiCheckArrayList)
    }

    fun addItem(item: DdayCheckList) {
        certiCheckArrayList.add(DdayCheckList(item.textCheck))
        certiCheckItemList.value=certiCheckArrayList
        android.util.Log.e("makeUs", "checkArrayList: $certiCheckArrayList")
    }

    fun removeItem(position: Int) {
        certiCheckArrayList.removeAt(position)
        certiCheckItemList.value = certiCheckArrayList
        android.util.Log.e("makeUs", "checkArrayList: $certiCheckArrayList")
    }

    private fun date(): String? {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR).toString() + "년 " + (today.get(Calendar.MONTH) + 1).toString() + "월 "+ today.get(
            Calendar.DAY_OF_MONTH).toString() + "일"
    }

}