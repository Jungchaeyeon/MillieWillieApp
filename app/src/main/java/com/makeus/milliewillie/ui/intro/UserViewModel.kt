package com.makeus.milliewillie.ui.intro

import androidx.lifecycle.MutableLiveData
import com.kakao.sdk.common.KakaoSdk.init
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.ServiceDetailType
import java.util.*

class UserViewModel : BaseViewModel() {

    val liveDateButtonList = List(5) { MutableLiveData<String>().apply {
        value =today() }}

        val liveTypeDetailList = MutableLiveData<List<ServiceDetailType>>()


        fun today(): String {
            val today = Calendar.getInstance()
            return today.get(Calendar.YEAR)
                .toString() + "." + (today.get(Calendar.MONTH) + 1).toString() + "." + today.get(
                Calendar.DAY_OF_MONTH
            ).toString()
        }


        fun requestDetailSoldier() {
            liveTypeDetailList.postValue(
                listOf(
                    ServiceDetailType("육군"),
                    ServiceDetailType("해군"),
                    ServiceDetailType("공군"),
                    ServiceDetailType("해병대"),
                    ServiceDetailType("의경"),
                    ServiceDetailType("카투사"),
                    ServiceDetailType("특전사"),
                    ServiceDetailType("의무소방"),
                    ServiceDetailType("해양의무경찰"),
                    ServiceDetailType("사회복무요원"),
                    ServiceDetailType("산업기능요원", "보충역"),
                    ServiceDetailType("산업기능요원", "현역"),
                    ServiceDetailType("전문연구요원")
                )
            )
        }

        fun requestDetailSergeant() {
            liveTypeDetailList.postValue(
                listOf(
                    ServiceDetailType("기술행정"),
                    ServiceDetailType("회전익 항공기"),
                    ServiceDetailType("항공운항"),
                    ServiceDetailType("통/번역"),
                    ServiceDetailType("특전")
                )
            )
        }

        fun requestDetailCaptain() {
            liveTypeDetailList.postValue(
                listOf(
                    ServiceDetailType("육군"), ServiceDetailType("해군"), ServiceDetailType("공군"),
                    ServiceDetailType("해병대")
                )
            )
        }

}

