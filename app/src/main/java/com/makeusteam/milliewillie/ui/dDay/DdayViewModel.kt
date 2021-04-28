package com.makeusteam.milliewillie.ui.dDay

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.login.LoginActivity
import java.util.*

class DdayViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) : BaseViewModel() {

    val liveDataDdayDate = MutableLiveData<String>().apply { value = " " }

    val liveDataDayGap = MutableLiveData<String>().apply { value = "날짜선택"}

    var deviceToken = LoginActivity.deviceToken

    fun getFcmToken(response: (String) -> Unit) {

    }

    private fun date(): String? {
        val today = Calendar.getInstance()
        return today.get(Calendar.YEAR).toString() + "년 " + (today.get(Calendar.MONTH) + 1).toString() + "월 "+ today.get(Calendar.DAY_OF_MONTH).toString() + "일"
    }

//    val dummyData = ScheduleRequest(color = "빨간색", distinction = "일정", title = "토익 인강듣기", startDate = "2021-03-09", endDate = "2021-03-10", repetition = "월", push = "T", pushDeviceToken = deviceToken)

//    fun requestSchedule() {
//        FirebaseMessaging.getInstance().token.addOnCompleteListener {
//            deviceToken = it.result ?: ""
//        }
//
//        apiRepository.schedule(dummyData)
//
//    }

//    fun requestSchedule() = apiRepository.schedule(dummyData)



}