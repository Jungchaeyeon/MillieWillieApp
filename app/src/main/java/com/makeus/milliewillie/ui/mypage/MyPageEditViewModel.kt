package com.makeus.milliewillie.ui.mypage

import android.annotation.SuppressLint
import android.icu.util.Calendar.DAY_OF_MONTH
import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.UsersPatch
import com.makeus.milliewillie.model.UsersResponse
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import java.util.Calendar.DAY_OF_MONTH
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class MyPageEditViewModel(
    val apiRepository: ApiRepository,
    val repositoryCached: RepositoryCached
) :
    BaseViewModel() {

    var usersResponse = UsersResponse.Result()
    var usersPatch = UsersPatch()
    val hobongClassInfo = MutableLiveData<String>()
    val allDday = MutableLiveData<String>()
    val monthProm = MutableLiveData<String>()
    val monthPromDday = MutableLiveData<String>()
    val nextProm = MutableLiveData<String>()
    val nextPromDday = MutableLiveData<String>()
    val formatNextMonthPromDate = MutableLiveData<String>()
    val formatNextPromDate = MutableLiveData<String>()
    val formatDischargeDate = MutableLiveData<String>()
    var toDischargePercent = MutableLiveData<String>()
    var toNextPromPercent = MutableLiveData<String>()
    var toMonthPromPercent = MutableLiveData<String>()
    var liveGoal = MutableLiveData<String>()
    var dischargePercent = 0.0f
    var nextPromPercent = 0.0f
    var monthPromPercent = 0.0f
    var userEnlist = MutableLiveData<String>()
    val dfParse = SimpleDateFormat("yyyy-MM-dd")

    val liveServicetype = MutableLiveData<String>()
    val liveDateButtonList = List(5) { MutableLiveData<String>() }

    init {
        getUsers()
        // 호봉 percent
        var count1 = dischargePercent
        var count2 = nextPromPercent
        var count3 = monthPromPercent
        Observable.interval(0, 100, TimeUnit.MILLISECONDS).timeInterval().map {
            count1++.toInt()
        }.subscribe {
            //퍼센트 계산
            toDischargePercent.postValue(dischargePercent.toString()+it.toString()+"%")

        }.disposeOnDestroy(this)
        Observable.interval(0, 100, TimeUnit.MILLISECONDS).timeInterval().map {
            count2++.toInt()
        }.subscribe {
            if(usersResponse.normalPromotionStateIdx !=3){
            //퍼센트 계산
            toNextPromPercent.postValue("$nextPromPercent$it%")}

        }.disposeOnDestroy(this)
        Observable.interval(0, 100, TimeUnit.MILLISECONDS).timeInterval().map {
            count3++.toInt()
        }.subscribe {
            //퍼센트 계산
            toMonthPromPercent.postValue(monthPromPercent.toString()+it.toString()+"%")

        }.disposeOnDestroy(this)
       monthPromPercent = HobongPercent().toFloat()
    }

    fun patchUsers() =
        apiRepository.patchUsers(
            UsersPatch(
                serveType = liveServicetype.value.toString(),
                startDate = usersResponse.startDate,
                endDate = usersResponse.endDate,
                strPrivate = usersResponse.strPrivate,
                strCorporal = usersResponse.strCorporal,
                strSergeant = usersResponse.strSergeant,
                proDate = null,
            )
        )
    fun patchGoal()=
        apiRepository.patchUsers(
            UsersPatch(
                serveType = null,
                startDate = null,
                endDate =null,
                strPrivate = null,
                strCorporal = null,
                strSergeant = null,
                proDate = null,
                goal =  liveGoal.value.toString()
            )
        )

    fun dateChangeTest(string: String): String {

        val date = string.substring(0, 10)
        val df = SimpleDateFormat("yyyy.MM.dd")
        val dff = SimpleDateFormat("yyyy-MM-dd")

        return dff.format(df.parse(date))
    }

    fun initMain() {
        val millidf = SimpleDateFormat("yyyy.MM.dd")
        userEnlist.value = millidf.format(dfParse.parse(usersResponse.startDate))
        allDday.value = " D - " + calDday(usersResponse.endDate).toString()
        if(calHobongDday() ==0){
            monthPromDday.value = " D - DAY"
        }else{
            monthPromDday.value = " D - " + calHobongDday().toString()}
        when (usersResponse.normalPromotionStateIdx) {
            0 -> {
                hobongClassInfo.value = "일병 "
                nextProm.value = "일병 "
                nextPromDday.value = "D - " + calDday(usersResponse.strPrivate).toString()
            }
            1 -> {
                hobongClassInfo.value = "일병 "
                nextProm.value = "상병 "
                nextPromDday.value = "D - " + calDday(usersResponse.strCorporal).toString()
            }
            2 -> {
                hobongClassInfo.value = "상병 "
                nextProm.value = "병장 "
                nextPromDday.value = "D - " + calDday(usersResponse.strSergeant).toString()
            }
            3 -> {
                hobongClassInfo.value = "병장 "
                nextProm.value = "다음 진급 없음"
                nextPromPercent=0.0f
            }
        }
        monthProm.value = hobongClassInfo.value+" "+usersResponse.hobong.plus(1).toString() + "호봉"
        hobongClassInfo.value =
            usersResponse.serveType +" "+ hobongClassInfo.value+" " + usersResponse.hobong.toString() + "호봉"
    }

    @SuppressLint("SimpleDateFormat")
    fun percentInit() {

        val dfFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        // Next percent계산하는 부분
        when (usersResponse.normalPromotionStateIdx) {
            0 -> {
                //다음 계급 percent
                nextPromPercent = dischargeDdayPercent(
                    usersResponse.startDate,
                    usersResponse.strPrivate
                )
                formatNextPromDate.value = dfFormat.format(dfParse.parse(usersResponse.strPrivate))
            }
            1 -> {
                nextPromPercent = dischargeDdayPercent(
                    usersResponse.strPrivate,
                    usersResponse.strCorporal
                )
                formatNextPromDate.value = dfFormat.format(dfParse.parse(usersResponse.strCorporal))
            }
            2 -> {
                nextPromPercent = dischargeDdayPercent(
                    usersResponse.strCorporal,
                    usersResponse.strSergeant
                )
                formatNextPromDate.value = dfFormat.format(dfParse.parse(usersResponse.strSergeant))
            }

            3 -> {
                toNextPromPercent.value =
                    dischargeDdayPercent(
                        usersResponse.strSergeant,
                        usersResponse.endDate
                    ).toString()
                formatNextPromDate.value = ""
            }
        }
        //다음달 1일 설정
        val cal = Calendar.getInstance(Locale.KOREA)
        cal.set(Calendar.DAY_OF_MONTH, 1); //다음 월의 1일로 변경
        cal.set(Calendar.MONTH, Calendar.MONTH + 2)
        Log.e(cal.get(Calendar.MONTH).toString(),"MONTH")
        Log.e(cal.get(Calendar.MONTH).plus(2).toString(),"MONTH+1")
        formatNextMonthPromDate.value = dfFormat.format(cal.time)

        //전역 yyyy년 MM월 dd일
        formatDischargeDate.value = dfFormat.format(dfParse.parse(usersResponse.endDate))

        dischargePercent = dischargeDdayPercent(usersResponse.startDate,usersResponse.endDate)
        repositoryCached.setValue(LocalKey.ALLDDAY, dischargePercent.toInt())
        repositoryCached.setValue(LocalKey.NEXTPROMDDAY, nextPromPercent.toInt())
        repositoryCached.setValue(LocalKey.MONTHPROMDDAY, monthPromPercent.toInt())

        toDischargePercent.value = dischargePercent.toString()
        toNextPromPercent.value = nextPromPercent.toString()
        toMonthPromPercent.value = monthPromPercent.toString()


        Log.e(dischargePercent.toString(), "DischargePercnet")
        Log.e(nextPromPercent.toString(), "NextPromPercent")
        Log.e(monthPromPercent.toString(), "NextMonthPromPercent")

        liveServicetype.value = usersResponse.serveType

        val df = SimpleDateFormat("yyyy.MM.dd (EE)")
        liveDateButtonList[0].value = df.format(dfParse.parse(usersResponse.startDate))
        liveDateButtonList[1].value = df.format(dfParse.parse(usersResponse.endDate))
        liveDateButtonList[2].value = df.format(dfParse.parse(usersResponse.strPrivate))
        liveDateButtonList[3].value = df.format(dfParse.parse(usersResponse.strCorporal))
        liveDateButtonList[4].value = df.format(dfParse.parse(usersResponse.strSergeant))

    }

    fun getUsers() = apiRepository.getUsersRes()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            Log.e("User정보 호출 성공")
            usersResponse = it.result
            initMain()
            percentInit()
            liveGoal.value =usersResponse.goal
            Log.e(usersResponse.toString(), "스타트")
        }, {
            it.printStackTrace()
        }).disposeOnDestroy(this)


    fun calDateBetweenAnB(date1: String, date2: String): Int {
        var calDateDays: Long = 0L
        val df = SimpleDateFormat("yyyy-MM-dd")
        try {
            val firstDate = df.parse(date1)
            val secondDate = df.parse(date2)

            var calDate: Long = firstDate.time - secondDate.time
            calDateDays = calDate / (24 * 60 * 60 * 1000)
            calDateDays = abs(calDateDays)

        } catch (e: ParseException) {
            Log.e("$calDateDays")
        }
        return calDateDays.toInt()
    }

    fun dischargeDdayPercent(date1: String, date2: String): Float {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val nowFormat = df.format(cal.time).toString() //오늘날짜

        val allDays = calDateBetweenAnB(date1, date2) //입대 ~ 전역
        val nowDays = calDateBetweenAnB(date1, nowFormat) // 입대 ~ 오늘

        return (nowDays * 100).div(allDays.toFloat()).toFloat()
    }

    fun calHobongDday(): Int {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val allMonthDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.e(allMonthDay.toString(),"이번달 최대일")
        val today = cal.get(Calendar.DATE).minus(1)

        Log.e(today.toString(),"오늘")
        Log.e((allMonthDay - today).toString(),"계산")
        return allMonthDay - (today)
    }


    fun calDday(inputDate: String): Int {
        var calDateDays: Int = 0
        val df = SimpleDateFormat("yyyy-MM-dd")
        try {
            val todayDate = Calendar.getInstance().time
            val calDate = df.parse(inputDate)

            var difference = todayDate.time - calDate.time
            calDateDays = (difference / (24 * 60 * 60 * 1000)).toInt()
            calDateDays = abs(calDateDays)
        } catch (e: ParseException) {

        }
        return calDateDays
    }

    fun HobongPercent(): Float {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val df = SimpleDateFormat("yyyy-MM-dd")
        val allMonthDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        val today = cal.get(Calendar.DATE)

        val allDays = allMonthDay // 전체 날
        val nowDays = allMonthDay - (allMonthDay - today)// 첫날 ~ 오늘
//        Log.e(today.toString(),"계산2")
//        Log.e(nowDays.toString(),"계산")
        return (nowDays * 100.toFloat() / allDays.toFloat())
    }

    fun enlistValueTest(): Boolean {

        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy.MM.dd (EE)")
        val firstDate = df.parse(liveDateButtonList[0].value.toString())
        val prom1Date = df.parse(liveDateButtonList[2].value.toString())
        val prom2Date = df.parse(liveDateButtonList[3].value.toString())
        val prom3Date = df.parse(liveDateButtonList[4].value.toString())
        val endDate = df.parse(liveDateButtonList[1].value.toString())

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
}
