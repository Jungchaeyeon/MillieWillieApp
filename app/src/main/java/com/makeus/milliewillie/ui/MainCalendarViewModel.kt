package com.makeus.milliewillie.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.CalendarDayResponse
import com.makeus.milliewillie.model.Main
import com.makeus.milliewillie.model.MainCalendarResponse
import com.makeus.milliewillie.model.UsersResponse
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
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

    fun getMainCalendar(response: (Boolean) -> Unit) = apiRepository.getMainCalendar(month = month)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            if (it.isSuccess) {
                planCalendar = it.result.planCalendar
                Log.e(planCalendar.toString(), "getMainCalendarTest")
                response.invoke(true)

            } else {
                Log.e("안들어옴")
                response.invoke(false)
            }
        }
        .disposeOnDestroy(this)
    fun getMainCalendarDay(response: (Boolean) -> Unit)= apiRepository.getMainCalendarDay(date = date)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            if (it.isSuccess) {
                planDayCalendar = it.result.planCalendar
               Log.e(it.result.toString(),"planDayCalendar 날별")
                addAllItem(it.result.plan)
                Log.e(liveMainPlan.value.toString(),"list 목록")
                response.invoke(true)

            } else {
                Log.e("false")
                response.invoke(false)
            }
        }
        .disposeOnDestroy(this)
}
