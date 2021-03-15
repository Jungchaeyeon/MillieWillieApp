package com.makeus.milliewillie.ui.dDay.birthday

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.DdayCheckList
import com.makeus.milliewillie.model.Intro

class BirthdayViewModel : BaseViewModel() {
    val checkItemList = MutableLiveData<List<DdayCheckList>>()

    init{
        requestIntroItemList()
    }
    fun requestIntroItemList() {
        checkItemList.postValue(
            listOf(
                DdayCheckList("에어팟"),
                DdayCheckList("생일 케이크")

            )
        )
    }


}