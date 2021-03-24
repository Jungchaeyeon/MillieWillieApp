package com.makeus.milliewillie.ui.plan

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.model.Plan


class MakePlanViewModel : BaseViewModel() {

    val livePlanType = MutableLiveData<String>().apply { value = " 일정" }
    val livePlanColor = MutableLiveData<String>().apply { value = "#8a6fff" }
    val liveDayAndNight = MutableLiveData<String>()
    val liveDate = MutableLiveData<String>().apply { value = "날짜선택" }
    val liveGoalData = MutableLiveData<String>()
    val livePlanTypeList = MutableLiveData<List<String>>()

    val liveAvailNumber = MutableLiveData<String>()
    var liveAvailHap =MutableLiveData<Int>().apply { value=0 }
    var liveAvailValue = List(3){MutableLiveData<String>().apply { value="0"}}

    // TodoItem list
    val livePlanTodoList = MutableLiveData<MutableList<Plan.Todos>>()
    var planTodos = ArrayList<Plan.Todos>()

    //TodoMethod
    fun addTodo(item: Plan.Todos) {
        planTodos.add(item)
        livePlanTodoList.value = planTodos
    }

    fun removeTodo(item: MainSchedule) {
        planTodos.remove(item)
        livePlanTodoList.value = planTodos
    }


    //Main 일정 recyclerview itemlist
    val liveMainPlan = MutableLiveData<ArrayList<MainSchedule>>().apply {
        this.postValue(
            arrayListOf(
                MainSchedule()
            )
        )
    }
    var planItems = ArrayList<MainSchedule>()

    //Main 일정 itemMethod
    fun addItem(item: MainSchedule) {

        if (planItems.size == 0) {
            planItems.add(0, MainSchedule())
            planItems.add(item)
            liveMainPlan.value = planItems
        } else {
            planItems.add(item)
            liveMainPlan.value = planItems
        }
    }

    fun removeItem(item: MainSchedule) {
        planItems.remove(item)
        liveMainPlan.value = planItems
    }

    fun notifyChange() {
        val items: ArrayList<MainSchedule>? = liveMainPlan.value
        liveMainPlan.value = items
    }

    fun requestPlanTypeList() {
        livePlanTypeList.postValue(
            listOf(
                "일정", "휴가",
                "외박", "훈련", "면회",
                "외출", "전투휴무", "당직"
            )
        )
    }
}





