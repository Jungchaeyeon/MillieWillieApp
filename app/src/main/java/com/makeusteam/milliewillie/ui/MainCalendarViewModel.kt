package com.makeusteam.milliewillie.ui

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.model.CalendarDayResponse
import com.makeusteam.milliewillie.model.MainCalendarResponse
import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*

class MainCalendarViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) :
    BaseViewModel() {

    lateinit var mainCalendar : MainCalendarResponse.Result
    lateinit var planCalendar : List<MainCalendarResponse.Result.PlanCalendar>
    lateinit var planDayCalendar : List<CalendarDayResponse.Result.PlanCalendar>

    val cal = Calendar.getInstance(TimeZone.getDefault())
    val df = SimpleDateFormat("yyyyMM")
    val dfDay =SimpleDateFormat("yyyyMMdd")
    var month = df.format(cal.time)
    var date = df.format(cal.time)
    var pickDay = MutableLiveData<String>()

    //CalendarView rv
    val liveMainPlan = MutableLiveData<ArrayList<CalendarDayResponse.Result.Plan>>()
    var planItems = ArrayList<CalendarDayResponse.Result.Plan>()

    fun addItem(item: CalendarDayResponse.Result.Plan) {

        planItems.add(item)
        liveMainPlan.value = planItems

    }

    fun addAllItem(item: List<CalendarDayResponse.Result.Plan>) {
        planItems.addAll(item)
        liveMainPlan.value = planItems

    }

    fun removeItem(item: List<CalendarDayResponse.Result.Plan>, i: Int) {
        planItems.remove(item[i])
        liveMainPlan.value = planItems
    }
    fun removeAllItems() {
        planItems.clear()
        liveMainPlan.value = planItems
    }

    fun getMainCalendar(response: (Boolean) -> Unit) = apiRepository.getMainCalendar(month = month)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            if (it.isSuccess) {
                planCalendar = it.result.planCalendar
                Log.e(planCalendar.toString(), "getMainCalendarTest")
                response.invoke(true)

            } else {
                Log.e("????????????")
                response.invoke(false)
            }
        }
        .disposeOnDestroy(this)
    fun getMainCalendarDay(response: (Boolean) -> Unit)= apiRepository.getMainCalendarDay(date = date)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            if (it.isSuccess) {
                if(!it.result.planCalendar.isNullOrEmpty()) {
                    planDayCalendar = it.result.planCalendar
                    Log.e(it.result.toString(), "planDayCalendar ??????")
                    addAllItem(it.result.plan!!)
                    Log.e(liveMainPlan.value.toString(), "list ??????")
                    response.invoke(true)
                }
            } else {
                Log.e("false")
                response.invoke(false)
            }
        }
        .disposeOnDestroy(this)
}
