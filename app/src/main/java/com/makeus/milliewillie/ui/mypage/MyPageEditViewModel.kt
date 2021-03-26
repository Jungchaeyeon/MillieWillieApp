package com.makeus.milliewillie.ui.mypage

import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.UsersResponse
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class MyPageEditViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) :
    BaseViewModel() {

    var usersResponse = UsersResponse.Result()
    val hobongClassInfo = MutableLiveData<String>()
    val allDday = MutableLiveData<String>()
    val monthProm = MutableLiveData<String>()
    val monthPromDday = MutableLiveData<String>()
    val nextProm = MutableLiveData<String>()
    val nextPromDday = MutableLiveData<String>()
    val formatNextMonthPromDate =MutableLiveData<String>()
    val formatNextPromDate =MutableLiveData<String>()
    val formatDischargeDate =MutableLiveData<String>()
    var toDischargePercent =MutableLiveData<String>()
    var toNextPromPercent =MutableLiveData<String>()
    var toMonthPromPercent =MutableLiveData<String>()
    var dischargePercent = 0
    var nextPromPercent = 0
    var monthPromPercent =0
    var userEnlist = MutableLiveData<String>()


    init {
        getUsers()
        Log.e(usersResponse.startDate)
        //userEnlist.value = usersResponse.startDate
    }
    fun initMain(){
        usersResponse.hobong+=2
        userEnlist.value = usersResponse.startDate
        allDday.value =  "D - "+calDday(usersResponse.endDate).toString()
        monthProm.value = usersResponse.hobong.plus(1).toString()+"호봉 진급"
        monthPromDday.value = "D - " + calHobongDday().toString()
        when(usersResponse.normalPromotionStateIdx){
            0-> {hobongClassInfo.value= "일병"
                nextProm.value = "일병진급"
                nextPromDday.value= "D -"+calDday(usersResponse.strPrivate).toString()  }
            1-> {hobongClassInfo.value= "일병"
                nextProm.value = "상병진급"
                nextPromDday.value= "D -"+calDday(usersResponse.strCorporal).toString()  }
            2-> {hobongClassInfo.value= "상병"
                nextProm.value = "병장진급"
                nextPromDday.value= "D -"+calDday(usersResponse.strSergeant).toString()  }
            3-> { hobongClassInfo.value= "병장"
                nextProm.value = "다음 진급 없음"}
        }
        hobongClassInfo.value =usersResponse.serveType+" "+ usersResponse.hobong.toString()+"호봉"
    }

    fun percentInit(){

        val dfParse = SimpleDateFormat("yyyy-MM-dd")
        val dfFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        // Next percent계산하는 부분
        when(usersResponse.normalPromotionStateIdx){
            0 -> { toNextPromPercent.value = dischargeDdayPercent(usersResponse.startDate,usersResponse.strPrivate).toString()
                   formatNextPromDate.value = dfFormat.format(dfParse.parse(usersResponse.strPrivate))}
            1 -> { toNextPromPercent.value = dischargeDdayPercent(usersResponse.strPrivate,usersResponse.strCorporal).toString()
                   formatNextPromDate.value = dfFormat.format(dfParse.parse(usersResponse.strCorporal))}
            2 ->{ toNextPromPercent.value = dischargeDdayPercent(usersResponse.strCorporal,usersResponse.strSergeant).toString()
                formatNextPromDate.value = dfFormat.format(dfParse.parse(usersResponse.strSergeant))}

            3 -> toNextPromPercent.value = dischargeDdayPercent(usersResponse.strSergeant,usersResponse.endDate).toString()
        }
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH,1); //다음 월의 1일로 변경
        cal.set(Calendar.MONTH,Calendar.MONTH+1)
        formatNextMonthPromDate.value =dfFormat.format(cal.time)
        Log.e(formatNextMonthPromDate.value,"포맷결과")
     //   formatNextMonthPromDate.value = dfFormat.format(cal)

        //전역 yyyy년 MM월 dd일
        formatDischargeDate.value = dfFormat.format(dfParse.parse(usersResponse.endDate))
        //전역 percent
        toDischargePercent.value = dischargeDdayPercent(usersResponse.startDate, usersResponse.endDate).toString()

        dischargePercent = toDischargePercent.value!!.toInt()
        nextPromPercent = toNextPromPercent.value!!.toInt()
        monthPromPercent = toMonthPromPercent.value!!.toInt()
        Log.e(toDischargePercent.value,"toDischargePercnet")
        Log.e(toDischargePercent.value,"toNextPromPercent")
        Log.e(toMonthPromPercent.value,"toNextMonthPromPercent")
    }

    fun getUsers() = apiRepository.getUsers().observeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe ({
            Log.e("User정보 호출 성공")
            usersResponse = it.result
            initMain()
            percentInit()
            Log.e(usersResponse.toString(),"스타트")
        } , {
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

//         Log.e(date1,"입대일")
//         Log.e(nowFormat,"오늘")
//         Log.e(allDays.toString(),"입전")
//         Log.e(nowDays.toString(),"입지")

        return (nowDays*100).div(allDays.toFloat()).toFloat()
    }

    fun calHobongDday(): Int {
        val cal =Calendar.getInstance()
        val allMonthDay =Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
      //  Log.e(allMonthDay.toString(),"이번달 최대일")
        val today = allMonthDay-Calendar.DATE
       // Log.e(today.toString(),"오늘")
        return allMonthDay-today
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

}
