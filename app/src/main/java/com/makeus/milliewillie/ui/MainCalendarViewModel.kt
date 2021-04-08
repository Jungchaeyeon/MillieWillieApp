package com.makeus.milliewillie.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
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

    val cal = Calendar.getInstance(TimeZone.getDefault())
    val df = SimpleDateFormat("yyyyMM")
    var month = df.format(cal.time)

    //CalendarView rv
    val liveMainPlan = MutableLiveData<ArrayList<MainCalendarResponse.Result.PlanCalendar>>()
    var planItems = ArrayList<MainCalendarResponse.Result.PlanCalendar>()

    fun addItem(item: MainCalendarResponse.Result.PlanCalendar) {

        planItems.add(item)
        liveMainPlan.value = planItems

    }

    fun addAllItem(item: List<MainCalendarResponse.Result.PlanCalendar>) {
        planItems.addAll(item)
        liveMainPlan.value = planItems

    }

    fun removeItem(item: List<MainCalendarResponse.Result.PlanCalendar>, i: Int) {
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

}
