package com.makeusteam.milliewillie.ui.holiday

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.model.VacationIdPatch
import com.makeusteam.milliewillie.model.VacationIdResponse
import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers

class HoliViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) :
    BaseViewModel() {

    var pickableMax = 35

    var regularHoliNum = 0
    var prizeHoliNum = 0
    var otherHoliNum = 0
    var regularNum = 0
    var prizeNum = 0
    var otherNum = 0

    var vacationIdPatch = VacationIdPatch()
    lateinit var vacationIdResponse: VacationIdResponse

    val liveAlreadyUseDays = MutableLiveData<Int>().apply { value = 0 }
    val liveNotUseDays = MutableLiveData<Int>().apply { value = 0 }
    val liveRegularHoliday = MutableLiveData<String>()
    val liveRegularWholeHoliday = MutableLiveData<String>()
    val livePrizeHoliday = MutableLiveData<String>()
    val livePrizeWholeHoliday = MutableLiveData<String>()
    val liveOtherHoliday = MutableLiveData<String>()
    val liveOtherWholeHoliday = MutableLiveData<String>()


    init {
       // getVacation()
    }


    //activity_holiday_edit
    val liveHoliType = MutableLiveData<String>()

    fun getVacation(response: (Boolean) -> Unit) {
        apiRepository.getVacation().observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isSuccess) {
                    vacationIdResponse = it

                    repositoryCached.setValue(LocalKey.VAC1ID,it.result[0].vacationId)
                    repositoryCached.setValue(LocalKey.VAC2ID,it.result[1].vacationId)
                    repositoryCached.setValue(LocalKey.VAC3ID,it.result[2].vacationId)
                    liveRegularHoliday.value = it.result[0].useDays.toString() + "일 /"
                    liveRegularWholeHoliday.value = it.result[0].totalDays.toString() + "일"
                    livePrizeHoliday.value = it.result[1].useDays.toString() + "일 /"
                    livePrizeWholeHoliday.value = it.result[1].totalDays.toString() + "일"
                    liveOtherHoliday.value = it.result[2].useDays.toString() + "일 /"
                    liveOtherWholeHoliday.value = it.result[2].totalDays.toString() + "일"

                    liveNotUseDays.value =
                        it.result[0].totalDays + it.result[1].totalDays + it.result[2].totalDays
                    liveAlreadyUseDays.value =
                        (it.result[0].totalDays + it.result[1].totalDays + it.result[2].totalDays).minus(it.result[0].useDays + it.result[1].useDays + it.result[2].useDays)


                    regularHoliNum = it.result[0].totalDays
                    prizeHoliNum = it.result[1].totalDays
                    otherHoliNum = it.result[2].totalDays
                    regularNum = it.result[0].useDays
                    prizeNum= it.result[1].useDays
                    otherNum= it.result[2].useDays
                    response.invoke(true)
                } else {
                    response.invoke(false)
                    Log.e("User정보 호출 실패")
                }
            }, {
                it.printStackTrace()
            }).disposeOnDestroy(this)
    }

    fun patchVacationId()=
        apiRepository.patchVacationId(
            VacationIdPatch(totalDays = vacationIdPatch.totalDays,useDays = vacationIdPatch.useDays),
            path = repositoryCached.getVacaId().toLong())

    fun patchVacationUseDays() =
        apiRepository.patchVacationId(
            VacationIdPatch(totalDays = null,useDays = vacationIdPatch.useDays),
            path = repositoryCached.getVacaId().toLong()
        ).subscribe({
            if(it.isSuccess){
                Log.e("통신 성공")
            }
            else{
                Log.e("통신실패")
            }
        },{Log.e("통신 실패")})


}