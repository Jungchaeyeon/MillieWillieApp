package com.makeus.milliewillie.ui.plan

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.Plan

class MakePlanViewModel : BaseViewModel() {

    val livePlanTypeList =MutableLiveData<List<String>>()
    val livePlanType = MutableLiveData<String>().apply { value = " 일정"}
    val livePlanColor = MutableLiveData<String>()
    val livePlanTodoList = MutableLiveData<List<Plan.Todos>>()


    fun requestPlanTypeList(){
        livePlanTypeList.postValue(listOf(
            "일정","정기휴가","포상휴가",
            "외박","훈련","면회",
            "외출","전투휴무","당직"
        ))
    }
    fun requestPlanTodoList(){
        livePlanTodoList.postValue(listOf(
            Plan.Todos(),
            Plan.Todos("나갈 때 닥터지"),
            Plan.Todos(" 챙길물품, 확인해야할 사항 기록!")
        ) )
    }

}




