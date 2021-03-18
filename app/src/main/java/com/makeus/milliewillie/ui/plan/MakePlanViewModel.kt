package com.makeus.milliewillie.ui.plan

import androidx.databinding.adapters.NumberPickerBindingAdapter.setValue
import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.model.Plan
import com.makeus.milliewillie.repository.local.RepositoryCached
import io.reactivex.internal.util.NotificationLite.getValue
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject


class MakePlanViewModel : BaseViewModel() {

    val livePlanTypeList = MutableLiveData<List<String>>()
    val livePlanType = MutableLiveData<String>().apply { value = " 일정" }
    val livePlanColor = MutableLiveData<String>().apply { value = "#8a6fff" }
    val livePlanTodoList = MutableLiveData<MutableList<Plan.Todos>>()
    val liveDayAndNight = MutableLiveData<String>()
    val liveDate = MutableLiveData<String>().apply { value = "날짜선택" }
    val liveGoalData = MutableLiveData<String>()

    // 메인 일정 recyclerview itemlist
    val liveMainPlan = MutableLiveData<ArrayList<MainSchedule>>().apply {
        this.postValue(
            arrayListOf(
                MainSchedule()
            )
        )
    }
    var planitems = ArrayList<MainSchedule>()

    fun additem(item: MainSchedule) {

        if (planitems.size == 0) {
            planitems.add(0, MainSchedule())
            planitems.add(item)
            liveMainPlan.value = planitems
        } else {
            planitems.add(item)
            liveMainPlan.value = planitems
        }
    }

    fun removeitem(item: MainSchedule) {
        planitems.remove(item)
        liveMainPlan.value = planitems
    }

    fun notifyChange() {
        val items: ArrayList<MainSchedule>? = liveMainPlan.value
        liveMainPlan.value = items
    }

    fun requestTodoList() {
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





