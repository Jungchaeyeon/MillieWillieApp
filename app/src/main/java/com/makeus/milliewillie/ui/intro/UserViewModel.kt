package com.makeus.milliewillie.ui.intro

import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.KakaoRequest
import com.makeus.milliewillie.model.ServiceDetailType
import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class UserViewModel(val repositoryCached: RepositoryCached, val apiRepository: ApiRepository) :
    BaseViewModel() {

    val liveUserName = MutableLiveData<String>()
    val liveUserBirth = MutableLiveData<String>().apply { value = "1999-07-21" }
    val liveServiceId = MutableLiveData<Int>().apply { value = 1 }
    val liveServicetype = MutableLiveData<String>().apply { value = "육군" }
    val liveUserGoal = MutableLiveData<String>()
    val liveEditData = MutableLiveData<String>()
    val liveDateButtonList = List(5) { MutableLiveData<String>().apply { value = today() } }
    val liveTypeDetailList = MutableLiveData<List<ServiceDetailType>>()

    val liveModifyTitle = MutableLiveData<String>().apply { value = "이름" }
    var usersRequest: UsersRequest = UsersRequest()

    fun enlistDataInit() {
        if (liveServiceId.value == 1) {
            calculateDay(today())
            liveDateButtonList[0].value = today()
        } else {
            Log.e("장교, 부사관 ")
            liveDateButtonList[1].value = ""
            liveDateButtonList[2].value = ""
        }

    }

    fun today(): String {
        val today = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy.MM.dd (EE)")
        return df.format(today)
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

    fun enlistValueTest(): Boolean {

        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy.MM.dd (EE)")
        val firstDate = df.parse(liveDateButtonList[0].value.toString())
        val prom1Date = df.parse(liveDateButtonList[2].value.toString())
        val prom2Date = df.parse(liveDateButtonList[3].value.toString())
        val prom3Date = df.parse(liveDateButtonList[4].value.toString())
        val endDate = df.parse(liveDateButtonList[1].value.toString())

//        Log.e("${cal.time}")
//        Log.e("$firstDate")
//        Log.e("$prom1Date?")
//        Log.e("$prom2Date")
//        Log.e("$prom3Date?")
//        Log.e("$endDate?")

        if (firstDate <= cal.time) {
            if (firstDate.time < prom1Date.time) {
                if (prom1Date.time < prom2Date.time) {
                    if (prom2Date.time < prom3Date.time) {
                        if (prom3Date.time < endDate.time) {
                            return true
                        }
                        return false
                    }
                    return false
                }
                return false
            }
            return false
        }
        return false
    }

    fun enlistValueTestSergeant(): Boolean {

        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy.MM.dd (EE)")
        val firstDate = df.parse(liveDateButtonList[0].value.toString())
        val prom1Date = df.parse(liveDateButtonList[2].value.toString())
        val endDate = df.parse(liveDateButtonList[1].value.toString())

        if (firstDate <= cal.time) {
            if (firstDate.time < prom1Date.time) {
                if (prom1Date.time < endDate.time) {
                    return true
                }
                return false
            }
            return false
        }
        return false
    }

    fun sergeantNull(){
        usersRequest.strPrivate=null
        usersRequest.strCorporal=null
        usersRequest.strSergeant=null
    }
    fun dateChangeTest(string: String): String {

        val date = string.substring(0, 10)
        val df = SimpleDateFormat("yyyy.MM.dd")
        val dff = SimpleDateFormat("yyyy-MM-dd")

        return dff.format(df.parse(date))
    }

    fun calculateDay(enlist: String) {

        //입대일 받아옴
        val getDateFormat = SimpleDateFormat("yyyy.MM.dd")
        val setDateFormat = SimpleDateFormat("yyyy.MM.dd (EE)")
        val date = getDateFormat.parse(enlist)
        val cal = Calendar.getInstance()
        cal.time = date

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

        when (repositoryCached.getTypeDetail()) {
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
            "카투사","특전사" -> {
                durPrivate = 0;durCorporal = 0;durSergeant = 0;durAll = 18
            }
            "해양의무경찰", "의무소방" -> {
                durPrivate = 0;durCorporal = 0;durSergeant = 0;durAll = 20
            }
        }
        //전역일
        cal.add(Calendar.MONTH, durAll)
        dischargeDate = setDateFormat.format(cal.time).toString()
        Log.e(repositoryCached.getTypeDetail().toString(),"type")
        Log.e(dischargeDate.toString(),"dischargeDate")
        Log.e(cal.time.toString(),"dischargeCal")
        cal.time = date

        //일병진급일
        cal.add(Calendar.MONTH, durPrivate)
        promPrivate = setDateFormat.format(cal.time).toString()

        //상병진급일
        cal.add(Calendar.MONTH, durCorporal)
        promCorporal = setDateFormat.format(cal.time).toString()

        //병장진급일
        cal.add(Calendar.MONTH, durSergeant)
        promSergeant = setDateFormat.format(cal.time).toString()
        cal.time = date

        liveDateButtonList[1].postValue(dischargeDate)
        liveDateButtonList[2].postValue(promPrivate)
        liveDateButtonList[3].postValue(promCorporal)
        liveDateButtonList[4].postValue(promSergeant)

        usersRequest.startDate = dateChangeTest(enlist)
        usersRequest.endDate = dateChangeTest(dischargeDate)
        usersRequest.strPrivate = dateChangeTest(promPrivate)
        usersRequest.strCorporal = dateChangeTest(promCorporal)
        usersRequest.strSergeant = dateChangeTest(promSergeant)
        usersRequest.proDate = null
    }

    fun calDateBetweenAnB(date1: String, date2: String): Float {
        var calDateDays: Long = 0L
        val df = SimpleDateFormat("yyyy.MM.dd (EE)")

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

    fun dischargeDdayPercent(): Float {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy.MM.dd (EE)")
        val nowFormat = df.format(cal.time).toString()

        val allDays = calDateBetweenAnB(
            liveDateButtonList[1].value.toString(),
            liveDateButtonList[0].value.toString()
        )
        val nowDays = calDateBetweenAnB(nowFormat, liveDateButtonList[0].value.toString())

        // Log.e((nowDays/allDays*100).toString(),"값")
        return (nowDays / allDays * 100).toFloat()
    }

    fun calDday(inputDate: String): Int {
        var calDateDays: Int = 0
        val df = SimpleDateFormat("yyyy.MM.dd (EE)")

        try {
            val todayDate = df.parse(today())
            val calDate = df.parse(inputDate)

            var difference = todayDate.time - calDate.time
            calDateDays = (difference / (24 * 60 * 60 * 1000)).toInt()
            calDateDays = abs(calDateDays)
        } catch (e: ParseException) {

        }
        return calDateDays
    }

    override fun onResume() {
        super.onResume()

    }

    //   fun requestUserUpdate() =
//        apiRepository.users(
//            UsersRequest(
//                name = liveUserName.value.toString(),
//                stateIdx = liveServiceId.value as Int,
//                serveType =liveServicetype.value.toString(),
//                startDate = liveDateButtonList[0].value.toString(),
//                endDate = liveDateButtonList[1].value.toString(),
//                strPrivate = liveDateButtonList[2].value.toString(),
//                strCorporal = liveDateButtonList[3].value.toString(),
//                strSergeant = liveDateButtonList[4].value.toString(),
//                proDate = liveDateButtonList[2].value.toString(),
//                goal = liveUserGoal.value.toString(),
//                socialType =
//            )
//        )
    fun requestUser() =
        apiRepository.users(
            UsersRequest(
                name = usersRequest.name,
                stateIdx = usersRequest.stateIdx,
                serveType = usersRequest.serveType,
                startDate = usersRequest.startDate,
                endDate = usersRequest.endDate,
                strPrivate = usersRequest.strPrivate,
                strCorporal = usersRequest.strCorporal,
                strSergeant = usersRequest.strSergeant,
                proDate = usersRequest.proDate,
                goal = usersRequest.goal,
                socialType = repositoryCached.getSocialType()
            )
        )


}

