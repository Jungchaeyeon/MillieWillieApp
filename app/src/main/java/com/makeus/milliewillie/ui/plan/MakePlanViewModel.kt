package com.makeus.milliewillie.ui.plan

import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.di.repositoryModule
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


class MakePlanViewModel(val repositoryCached: RepositoryCached, val apiRepository: ApiRepository) :
    BaseViewModel() {

    val livePlanType = MutableLiveData<String>().apply { value = "일정" }
    val livePlanColor = MutableLiveData<String>().apply { value = "#8a6fff" }
    val liveDayAndNight = MutableLiveData<String>()
    var liveDate = MutableLiveData<String>().apply { value = "날짜선택" }
    val liveGoalData = MutableLiveData<String>()
    val livePlanTypeList = MutableLiveData<List<String>>()
    var liveOnlyDay = MutableLiveData<String>()

    var regularHoliNum = 0
    var prizeHoliNum = 0
    var otherHoliNum = 0
    var regularNum = 0
    var prizeNum = 0
    var otherNum = 0

    val liveAlreadyUseDays = MutableLiveData<Int>().apply { value = 0 }
    val liveNotUseDays = MutableLiveData<Int>().apply { value = 0 }
    val liveRegularHoliday = MutableLiveData<String>()
    val liveRegularWholeHoliday = MutableLiveData<String>()
    val livePrizeHoliday = MutableLiveData<String>()
    val livePrizeWholeHoliday = MutableLiveData<String>()
    val liveOtherHoliday = MutableLiveData<String>()
    val liveOtherWholeHoliday = MutableLiveData<String>()

    var plansRequest = PlansRequest()
    lateinit var plans: Plans.Result

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


    //                "일정", "휴가",
    fun requestPlanTypeList() {
        livePlanTypeList.postValue(
            listOf(
                "일정",
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

    fun getVacation(response: (Boolean) -> Unit) {
        apiRepository.getVacation().observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccess) {


                    repositoryCached.setValue(LocalKey.VAC1ID, it.result[0].vacationId)
                    repositoryCached.setValue(LocalKey.VAC2ID, it.result[1].vacationId)
                    repositoryCached.setValue(LocalKey.VAC3ID, it.result[2].vacationId)
                    liveRegularHoliday.value = it.result[0].useDays.toString() + "일 /"
                    liveRegularWholeHoliday.value = it.result[0].totalDays.toString() + "일"
                    livePrizeHoliday.value = it.result[1].useDays.toString() + "일 /"
                    livePrizeWholeHoliday.value = it.result[1].totalDays.toString() + "일"
                    liveOtherHoliday.value = it.result[2].useDays.toString() + "일 /"
                    liveOtherWholeHoliday.value = it.result[2].totalDays.toString() + "일"

                    liveAlreadyUseDays.value =
                        it.result[0].useDays + it.result[1].useDays + it.result[2].useDays
                    liveNotUseDays.value =
                        it.result[0].totalDays + it.result[1].totalDays + it.result[2].totalDays

                    regularHoliNum = it.result[0].totalDays
                    prizeHoliNum = it.result[1].totalDays
                    otherHoliNum = it.result[2].totalDays
                    regularNum = it.result[0].useDays
                    prizeNum = it.result[1].useDays
                    otherNum = it.result[2].useDays
                    response.invoke(true)
                } else {
                    response.invoke(false)
                    Log.e("User정보 호출 실패")
                }
            }, {
                it.printStackTrace()
            }).disposeOnDestroy(this)
    }
}





