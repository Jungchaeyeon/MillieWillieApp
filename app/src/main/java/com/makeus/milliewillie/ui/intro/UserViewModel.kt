package com.makeus.milliewillie.ui.intro

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.ServiceDetailType
import com.makeus.milliewillie.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class UserViewModel : BaseViewModel() {

    val liveEditData = MutableLiveData<String>()
    val liveModifyTitle = MutableLiveData<String>().apply { value = "이름" }
    val liveUserName = MutableLiveData<String>().apply { value = "정채연" }
    val liveUserBirth = MutableLiveData<String>().apply { value = "1999.07.21" }
    val liveUserGoal = MutableLiveData<String>().apply { value = "토익만 따서 나가자!" }


    val liveServiceype = MutableLiveData<String>().apply { value = "육군" }
    val liveDateButtonList = List(5) { MutableLiveData<String>().apply { value = today() } }
    val liveTypeDetailList = MutableLiveData<List<ServiceDetailType>>()


    init {
        calculateDay(today())
    }

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

    fun calculateDay(enlist: String) {

        //enlist.showShortToastSafe()
        //입대일 받아옴
        val df = SimpleDateFormat("yyyy.MM.dd")
        val date = df.parse(enlist)
        val cal = Calendar.getInstance()
        cal.time = date

        // cal.time.toString().showShortToastSafe()
        // df.format(cal.time).toString().showShortToastSafe()

        //진급일
        var promPrivate = ""
        var promCorporal = ""
        var promSergeant = ""
        var dischargeDate = ""

        //진급기간
        var durPrivate = 0
        var durCorporal = 0
        var durSergeant = 6
        var durAll = 0

        //전역일
        var enlistDate = 0

        when (liveServiceype.value.toString()) {
            "육군" -> {
                durPrivate = 2;durCorporal = 6;durSergeant = 6;durAll = 18
            }
            "해군" -> {
                durPrivate = 2;durCorporal = 6;durSergeant = 6;durAll = 20
            }
            "공군" -> {
                durPrivate = 2;durCorporal = 6;durSergeant = 6;durAll = 22
            }
            "의경" -> {
                durPrivate = 2;durCorporal = 6;durSergeant = 6;durAll = 18
            }
            "해병대" -> {
                durPrivate = 2;durCorporal = 6;durSergeant = 6;durAll = 18
            }
            "카투사" -> {
                durPrivate = 0;durCorporal = 0;durSergeant = 0;durAll = 18
            }
            "해양의무경찰", "의무소방" -> {
                durPrivate = 0;durCorporal = 0;durSergeant = 0;durAll = 20
            }
        }
        //전역일
        cal.add(Calendar.MONTH, durAll)
        dischargeDate = df.format(cal.time).toString()
        cal.time = date

        //일병진급일
        cal.add(Calendar.MONTH, durPrivate)
        promPrivate = df.format(cal.time).toString()

        //상병진급일
        cal.add(Calendar.MONTH, durCorporal)
        promCorporal = df.format(cal.time).toString()

        //병장진급일
        cal.add(Calendar.MONTH, durSergeant)
        promSergeant = df.format(cal.time).toString()
        cal.time = date

        liveDateButtonList[1].postValue(dischargeDate)
        liveDateButtonList[2].postValue(promPrivate)
        liveDateButtonList[3].postValue(promCorporal)
        liveDateButtonList[4].postValue(promSergeant)
    }

    fun calDateBetweenAnB(date1: String, date2: String): Float {
        var calDateDays: Long = 0L
        val df = SimpleDateFormat("yyyy.MM.dd")

        try {
            val firstDate = df.parse(date1)
            val secondDate = df.parse(date2)

            var calDate: Long = firstDate.time - secondDate.time
            calDateDays = calDate / (24 * 60 * 60 * 1000)
            calDateDays = abs(calDateDays) as Long
        } catch (e: ParseException) {

        }
        return calDateDays.toFloat()
    }

    fun dischargeDday(): Float {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy.MM.dd")
        val nowFormat = df.format(cal.time).toString()


        val allDays = calDateBetweenAnB(
            liveDateButtonList[1].value.toString(),
            liveDateButtonList[0].value.toString()
        )
        val nowDays = calDateBetweenAnB(nowFormat, liveDateButtonList[0].value.toString())

       // Log.e((nowDays/allDays*100).toString(),"값")
        return (nowDays / allDays * 100)
    }

}

