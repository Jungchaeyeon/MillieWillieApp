package com.makeus.milliewillie.ui.plan

import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.repository.ApiRepository
import io.reactivex.Observable
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MakePlanViewModel(val apiRepository: ApiRepository) :
    BaseViewModel() {

    val livePlanType = MutableLiveData<String>().apply { value = "일정" }
    val livePlanColor = MutableLiveData<String>().apply { value = "#8a6fff" }
    val liveDayAndNight = MutableLiveData<String>()
    var liveDate = MutableLiveData<String>().apply { value = "날짜선택" }
    val liveGoalData = MutableLiveData<String>()
    val livePlanTypeList = MutableLiveData<List<String>>()
    var liveOnlyDay = MutableLiveData<String>()

    var plansRequest = PlansRequest()

    // TodoItem list
    val livePlanTodoList = MutableLiveData<ArrayList<PlansRequest.Work>>()
    var planTodos = ArrayList<PlansRequest.Work>()
    //TodoMethod
    fun addTodo(item: PlansRequest.Work) {
        planTodos.add(item)
        livePlanTodoList.value = planTodos
    }
    fun replaceTodo() {
        planTodos.clear()
        livePlanTodoList.value = planTodos
    }
    //showTodo
//    val livePlanTodoList = MutableLiveData<ArrayList<String>>()
//    var planTodosList = ArrayList<String>()
//    //TodoMethod
//    fun addTodoItem(item: String) {
//        planTodosList.add(item)
//        livePlanTodoList.value = planTodosList
//    }
//    fun replaceTodoItem() {
//        planTodosList.clear()
//        livePlanTodoList.value = planTodosList
//    }


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

    fun requestPlan() =
        apiRepository.plans(
            PlansRequest(
                color = livePlanColor.value.toString(),
                planType = livePlanType.value.toString(),
                title = plansRequest.title,
                startDate = plansRequest.startDate,
                endDate = plansRequest.endDate,
                push = plansRequest.push,
                pushDeviceToken = null,
                planVacation = plansRequest.planVacation,
                work = plansRequest.work
            )
        )
//    fun initDate(){
//        val today = Calendar.getInstance().time
//        plansRequest.startDate=planDateChange(today.toString())
//        plansRequest.endDate=planDateChange(today.toString())
//    }

    val liveDDayPercent = MutableLiveData<String>()
    var count = 0

    init {
        Observable.interval(0, 1, TimeUnit.SECONDS).timeInterval().map {
            count++
        }.subscribe {
            //퍼센트 계산
            liveDDayPercent.postValue(it.toString())

        }.disposeOnDestroy(this)
    }
}





