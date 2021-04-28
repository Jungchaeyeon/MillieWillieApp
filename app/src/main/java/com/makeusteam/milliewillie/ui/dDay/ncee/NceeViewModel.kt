package com.makeusteam.milliewillie.ui.dDay.ncee

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.model.DdayCheckList
import java.util.*

class NceeViewModel : BaseViewModel() {
    val liveDataToday = MutableLiveData<String>().apply { value = date() }
    val liveDataTodayInfo = MutableLiveData<String>().apply { value = "시작일" }

    val nceeCheckItemList = MutableLiveData<ArrayList<DdayCheckList>>()

    val nceeCheckArrayList = ArrayList<DdayCheckList>()

    init{
        defaultCheckItemList()
    }

    fun defaultCheckItemList() {
        nceeCheckItemList.postValue(nceeCheckArrayList)
    }

    fun addItem(item: DdayCheckList) {
        nceeCheckArrayList.add(DdayCheckList(item.textCheck))
        nceeCheckItemList.value = nceeCheckArrayList
        android.util.Log.e("makeUs", "checkArrayList: $nceeCheckArrayList")
    }

    fun removeItem(position: Int) {
        nceeCheckArrayList.removeAt(position)
        nceeCheckItemList.value = nceeCheckArrayList
        android.util.Log.e("makeUs", "checkArrayList: $nceeCheckArrayList")
    }



    private fun date(): String? {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR).toString() + "년 " + (today.get(Calendar.MONTH) + 1).toString() + "월 "+ today.get(
            Calendar.DAY_OF_MONTH).toString() + "일"
    }

}