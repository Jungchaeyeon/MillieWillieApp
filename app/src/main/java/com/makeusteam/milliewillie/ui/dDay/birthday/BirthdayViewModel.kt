package com.makeusteam.milliewillie.ui.dDay.birthday

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.model.DdayCheckList

class BirthdayViewModel : BaseViewModel() {
    val checkItemList = MutableLiveData<ArrayList<DdayCheckList>>()

    val checkArrayList = ArrayList<DdayCheckList>()

    init{
        defaultCheckItemList()
    }
    fun defaultCheckItemList() {
        checkItemList.postValue(checkArrayList)
    }

    fun addItem(item: DdayCheckList) {
        checkArrayList.add(DdayCheckList(item.textCheck))
        checkItemList.value=checkArrayList
        android.util.Log.e("makeUs", "checkArrayList: $checkArrayList")
    }

    fun removeItem(position: Int) {
        checkArrayList.removeAt(position)
        checkItemList.value = checkArrayList
        android.util.Log.e("makeUs", "checkArrayList: $checkArrayList")
    }

}