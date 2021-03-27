package com.makeus.milliewillie.ui.holiday

import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.VacationIdPatch
import com.makeus.milliewillie.model.VacationIdResponse
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers

class HoliViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) :
    BaseViewModel() {

    var pickableMax = 50

    var regularHoliNum = 0
    var prizeHoliNum = 0
    var otherHoliNum = 0

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
                    response.invoke(true)
                } else {
                    Log.e("User정보 호출 실패")
                    response.invoke(false)
                }
            }, {
                it.printStackTrace()
            }).disposeOnDestroy(this)
    }

    fun patchVacationId(response: (Boolean) -> Unit) =
        apiRepository.patchVacationId(
            VacationIdPatch(
                totalDays = vacationIdPatch.totalDays
            ),
            path = repositoryCached.getVacaId().toLong()
        ).subscribe({
            if (it.isSuccess) {
                Log.e("휴가 수정 성공")
                response.invoke(true)
            } else {
                "휴가 수정실패".showShortToastSafe()
                response.invoke(false)
            }
        }, {
            response.invoke(false)
        })


}