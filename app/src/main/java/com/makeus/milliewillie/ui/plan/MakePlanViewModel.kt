package com.makeus.milliewillie.ui.plan

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.Plan

class MakePlanViewModel : BaseViewModel() {

    val livePlanTypeList = MutableLiveData<List<String>>()
    val livePlanType = MutableLiveData<String>().apply { value = " 일정" }
    val livePlanColor = MutableLiveData<String>().apply { value ="#ffbe65" }
    val livePlanTodoList =MutableLiveData<MutableList<Plan.Todos>>()
    val liveDayAndNight =MutableLiveData<String>()
    val liveDate = MutableLiveData<String>().apply { value = "날짜선택" }


        fun requestTodoList(){
            livePlanTodoList.postValue(
                mutableListOf(
                    Plan.Todos("챙길 물품, 할일 적기")
                )
            )
        }

        fun requestPlanTypeList() {
            livePlanTypeList.postValue(
                listOf(
                    "일정", "정기휴가", "포상휴가",
                    "외박", "훈련", "면회",
                    "외출", "전투휴무", "당직"
                )
            )
        }


    }





