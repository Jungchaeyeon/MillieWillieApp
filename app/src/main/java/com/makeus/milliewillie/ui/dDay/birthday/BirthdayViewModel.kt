package com.makeus.milliewillie.ui.dDay.birthday

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.DdayCheckList
import com.makeus.milliewillie.model.Intro
import com.makeus.milliewillie.model.WorkoutSet

class BirthdayViewModel : BaseViewModel() {
    val checkItemList = MutableLiveData<ArrayList<DdayCheckList>>()

    val checkArrayList = ArrayList<DdayCheckList>()

    init{
        defaultCheckItemList()
    }
    fun defaultCheckItemList() {
        checkItemList.postValue(checkArrayList)

    }

    fun addItem(text: String) {
        checkArrayList.add(DdayCheckList(text))
        android.util.Log.e("makeUs", "checkArrayList: $checkArrayList")
    }

    fun removeItem(position: Int) {
        checkArrayList.removeAt(position)
        android.util.Log.e("makeUs", "checkArrayList: $checkArrayList")
    }


}