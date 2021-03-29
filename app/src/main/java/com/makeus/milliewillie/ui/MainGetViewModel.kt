package com.makeus.milliewillie.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.airbnb.lottie.parser.moshi.JsonReader
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.Main
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.model.UsersResponse
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class MainGetViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) :
    BaseViewModel() {


    lateinit var mainResponse : Main.Result

    val liveName = MutableLiveData<String>()
    val liveGoal = MutableLiveData<String>()
    val liveClass = MutableLiveData<String>()
    val liveHobongStr = MutableLiveData<String>()
    val allDday = MutableLiveData<String>()
    val monthProm = MutableLiveData<String>()
    val monthPromDday = MutableLiveData<String>()
    val nextProm = MutableLiveData<String>()
    val nextPromDday = MutableLiveData<String>()
    val enlistDayFormat = MutableLiveData<String>()

//    val formatNextMonthPromDate =MutableLiveData<String>()
//    val formatNextPromDate =MutableLiveData<String>()
//    val formatDischargeDate =MutableLiveData<String>()
//    val serviceInfo = MutableLiveData<String>()
//    var toDischargePercent = ""
//    var toNextPromPercent = ""
//    var toMonthPromPercent = ""
    var nowPercentInt = 0
    var nowPercentStr ="%"
    var nowPercentFlt = 0.0f

    init {
        getMain()
    }
//    fun initMain(){
//        usersResponse.hobong+=2
//
//        allDday.value =  "D - "+calDday(usersResponse.endDate).toString()
//        monthProm.value = usersResponse.hobong.plus(1).toString()+"호봉 진급"
//        monthPromDday.value = "D - " + calHobongDday().toString()
//        when(usersResponse.normalPromotionStateIdx){
//            0-> {hobongClassInfo.value= "일병"
//                nextProm.value = "일병진급"
//                nextPromDday.value= "D -"+calDday(usersResponse.strPrivate).toString()  }
//            1-> {hobongClassInfo.value= "일병"
//                nextProm.value = "상병진급"
//                nextPromDday.value= "D -"+calDday(usersResponse.strCorporal).toString()  }
//            2-> {hobongClassInfo.value= "상병"
//                nextProm.value = "병장진급"
//                nextPromDday.value= "D -"+calDday(usersResponse.strSergeant).toString()  }
//            3-> { hobongClassInfo.value= "병장"
//                nextProm.value = "다음 진급 없음"}
//        }
//        hobongClassInfo.value += usersResponse.hobong.toString()+"호봉"
//        nowPercentInt= dischargeDdayPercent(usersResponse.startDate,usersResponse.endDate).toInt()
//        nowPercentFlt= nowPercentInt.toFloat().plus(0.05).toFloat()
//        nowPercentStr= nowPercentInt.toString()+"%"
//        Log.e("${nowPercentInt}","nowPercentInt")
//    }

//    fun percentInit(){
//
//        val dfParse = SimpleDateFormat("yyyy-MM-dd")
//
//        //percent계산하는 부분
//
//        val cal = Calendar.getInstance()
//        cal.set(Calendar.DAY_OF_MONTH+1,1); //다음 월의 1일로 변경
//
//        Log.e(cal.time.toString())
//     //   formatNextMonthPromDate.value = dfFormat.format(cal)
//
//        formatDischargeDate.value = dfFormat.format(dfParse.parse(usersResponse.endDate))
//
//        Log.e(toDischargePercent,"toDischargePercnet")
//        Log.e(toDischargePercent,"toNextPromPercent")
//        Log.e(toMonthPromPercent,"toNextMonthPromPercent")
//
//        repositoryCached.setValue(LocalKey.ALLDDAY,nowPercentInt)
//    }


    fun getMain() = apiRepository.getMain()
        .subscribe ({
            Log.e("User정보 호출 성공")
            mainResponse = it.result
            Log.e(mainResponse.toString())
            liveName.value = mainResponse.name
            liveGoal.value = mainResponse.goal
            liveClass.value = this.initClass(mainResponse.stateIdx,mainResponse.normalPromotionStateIdx)
            liveHobongStr.value = mainResponse.hobong.toString() +"호봉"
            allDday.value =  "D - "+calDday(mainResponse.endDate).toString()
            monthProm.value = mainResponse.hobong.plus(1).toString() +"호봉 진급"
            monthPromDday.value = "D - "+ calHobongDday().toString()
            nextProm.value = this.initNextProm(mainResponse.normalPromotionStateIdx)
            nextPromDday.value = "D - "+ initNextPromDay(mainResponse.normalPromotionStateIdx)
            enlistDayFormat.value = dateFormat(mainResponse.endDate)
            nowPercentInt = dischargeDdayPercent(mainResponse.startDate,mainResponse.endDate).toInt()
            nowPercentStr =dischargeDdayPercent(mainResponse.startDate,mainResponse.endDate).toInt().toString()+"%"
            nowPercentFlt = (nowPercentInt.toFloat()/100.0).plus(0.025).toFloat()
            Log.e("$nowPercentFlt","nowPercentFlt")
            Log.e((nowPercentInt.toFloat()/10.0).toString(),"nowPercentt")


                    } , {
            it.printStackTrace()
        }).disposeOnDestroy(this)

    @SuppressLint("SimpleDateFormat")
    fun dateFormat(inputDate: String) : String{
        val dfParse = SimpleDateFormat("yyyy-MM-dd")
        val dfFormat = SimpleDateFormat("yyyy.MM.dd")

        return dfFormat.format(dfParse.parse(inputDate))
    }

    fun initNextPromDay(nowClass: Int) : Int{
        return when(nowClass){
            0-> calDday(mainResponse.strPrivate)
            1-> calDday(mainResponse.strCorporal)
            2-> calDday(mainResponse.strSergeant)
            3-> calDday(mainResponse.endDate)
            else -> 0
        }
    }
    fun initClass(serviceId: Int, nowClass : Int) : String{
       // if(serviceId == 0){
        return when(nowClass){
            0-> "일병"
            1-> "이병"
            2-> "상병"
            3-> "병장"
            else -> ""
        }
    //}
//        else{
//            return ""
//        }
    }
    fun initNextProm(nowClass: Int) : String{
        return when(nowClass){
            0-> "이병"
            1-> "상병"
            2-> "병장"
            3-> "전역"
            else -> ""
        }
    }
    //Main 일정 recyclerview itemlist
    val liveMainPlan = MutableLiveData<ArrayList<MainSchedule>>().apply {
        this.postValue(
            arrayListOf(
                MainSchedule()
            )
        )
    }
    var planItems = ArrayList<MainSchedule>()

    //Main 일정 itemMethod
    fun addItem(item: MainSchedule) {

        if (planItems.size == 0) {
            planItems.add(0, MainSchedule())
            planItems.add(item)
            liveMainPlan.value = planItems
        } else {
            planItems.add(item)
            liveMainPlan.value = planItems
        }
    }

    fun removeItem(item: MainSchedule) {
        planItems.remove(item)
        liveMainPlan.value = planItems
    }

    fun notifyChange() {
        val items: ArrayList<MainSchedule>? = liveMainPlan.value
        liveMainPlan.value = items
    }


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

    @SuppressLint("SimpleDateFormat")
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

        return (nowDays*100).div(allDays.toFloat())
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
