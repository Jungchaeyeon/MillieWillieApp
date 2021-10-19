package com.makeusteam.milliewillie.ui.home.tab1

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.model.main.*
import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.utils.ArmyInfoCalculationUtils
import com.makeusteam.milliewillie.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class HomeViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) :
    BaseViewModel() {

    val mainPageInfoMutableLiveData = MutableLiveData<MainPageModel>()
    var planItems = ArrayList<PlanMain>()
    var todayDate = MutableLiveData<String>()
    val liveMainPlan = MutableLiveData<ArrayList<PlanMain>>().apply {
        this.postValue(
            arrayListOf(
                PlanMain(0, "")
            )
        )
    }

    init{
        getHomeInfo{}
        initTodayDate()
    }


    fun getHomeInfo(response: (Boolean) -> Unit) = apiRepository.getHomeInfo()
        .subscribe({
            if (it.isSuccess) {
                it?.result?.let { respon ->

                    mainPageInfoMutableLiveData.value =  respon.toModel(
                        allDday = ArmyInfoCalculationUtils.totalDdayCalculation(respon.startDate,respon.endDate).toString(),
                        hodongDday = ArmyInfoCalculationUtils.hobongDdayCalculation().toString(),
                        promotionDday = nextPromotionDdayCalculation(respon.normalPromotionStateIdx).toString(),
                        nextClass = nextPromotionTitle(respon.normalPromotionStateIdx),
                        percent = ArmyInfoCalculationUtils.serviceTimePercent(respon.startDate,respon.endDate)
                    )
                    addAllItem(respon.plan)
                    response(true)
                }
            } else {
                if(it.code == 3100){
                    Log.e("해당 회원과 일치하는 메인 정보가 없습니다!")
                    repositoryCached.setValue(LocalKey.TOKEN,"")
                }
                response(false)
            }
        }, {
            it.printStackTrace()
        }).disposeOnDestroy(this)


    @SuppressLint("SimpleDateFormat")
    fun initTodayDate(){
        val calToday = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
        todayDate.value= calToday.get(Calendar.MONTH).plus(1).toString()+"월 "+calToday.get(Calendar.DATE)+"일"
    }


    fun nextPromotionDdayCalculation(nowClass: PromotionState): Int = with(ArmyInfoCalculationUtils){
        return when (nowClass) {
            PromotionState.PRIVATE -> dDayCalculation(mainPageInfoMutableLiveData.value?.strPrivate.toString())
            PromotionState.FIRSTCLASS -> dDayCalculation(mainPageInfoMutableLiveData.value?.strCorporal.toString())
            PromotionState.CORPORAL -> dDayCalculation(mainPageInfoMutableLiveData.value?.strSergeant.toString())
            PromotionState.SERGENT -> dDayCalculation(mainPageInfoMutableLiveData.value?.endDate.toString())
            PromotionState.DISCHARGE -> 0
        }
    }
    fun nextPromotionTitle(nowClass: PromotionState) : PromotionState {
        return when (nowClass) {
            PromotionState.PRIVATE -> PromotionState.FIRSTCLASS
            PromotionState.FIRSTCLASS -> PromotionState.CORPORAL
            PromotionState.CORPORAL -> PromotionState.SERGENT
            PromotionState.SERGENT -> PromotionState.DISCHARGE
            PromotionState.DISCHARGE -> PromotionState.DISCHARGE
        }
    }



    fun addItem(item:PlanMain) {

        if (planItems.size == 0) {
            planItems.add(0, PlanMain(0, ""))
            liveMainPlan.value = planItems
        } else {
            planItems.add(item)
            liveMainPlan.value = planItems
        }
    }

    fun addAllItem(item: List<PlanMain>) {

        if (planItems.size == 0) {
            planItems.add(0, PlanMain(0, ""))
            liveMainPlan.value = planItems
        } else {
            planItems.addAll(item)
            liveMainPlan.value = planItems
        }
    }

    fun removeItem(item: List<PlanMain>, i: Int) {
        planItems.remove(item[i])
        liveMainPlan.value = planItems
    }








}
