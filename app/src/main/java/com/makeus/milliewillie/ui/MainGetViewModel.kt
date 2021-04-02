package com.makeus.milliewillie.ui

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeus.milliewillie.R
import com.makeus.milliewillie.model.Main
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class MainGetViewModel(val apiRepository: ApiRepository, val repositoryCached: RepositoryCached) :
    BaseViewModel() {


    lateinit var mainResponse: Main.Result

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
    val vacUseDays = MutableLiveData<String>()
    val vacTotalDays = MutableLiveData<String>()
    var todayDate = MutableLiveData<String>()
    var nowPercentInt = 0
    var nowPercentStr = "%"
    var nowPercentFlt = 0.0f
    var stateIdx = 0
    var classImg = 0

    init {
        getMain()
        initTodayDate()
    }


    fun getMain() = apiRepository.getMain()
        .subscribe({
            if (it.isSuccess) {
                Log.e("User정보 호출 성공")
                mainResponse = it.result
                Log.e(mainResponse.toString())
                liveName.value = mainResponse.name
                liveGoal.value = mainResponse.goal
                userProfileImgUrl = mainResponse.profileImg
                Log.e("userProfileUrl = $userProfileImgUrl")
                if (it.result.stateIdx == 1){
                    nextProm.value = this.initNextProm(mainResponse.normalPromotionStateIdx)
                    nextPromDday.value = "D - "+ initNextPromDay(mainResponse.normalPromotionStateIdx)
                    liveClass.value = this.initClass(mainResponse.stateIdx,mainResponse.normalPromotionStateIdx)
                    liveHobongStr.value = mainResponse.hobong.toString() +"호봉"
                    monthProm.value = mainResponse.hobong.plus(1).toString() +"호봉 진급"
                }
                allDday.value = "D - " + calDday(mainResponse.endDate).toString()
                monthPromDday.value = "D - " + calHobongDday().toString()
                classImg = initImg(it.result.normalPromotionStateIdx)
                enlistDayFormat.value = dateFormat(mainResponse.endDate)
                vacUseDays.value = (mainResponse.vacationTotalDays-mainResponse.vacationUseDays).toString()
                vacTotalDays.value = mainResponse.vacationTotalDays.toString()
                if (mainResponse.stateIdx != 1) {

                }
                nowPercentInt = dischargeDdayPercent(mainResponse.startDate, mainResponse.endDate).toInt()
                nowPercentStr = dischargeDdayPercent(mainResponse.startDate, mainResponse.endDate).toInt().toString() + "%"
                nowPercentFlt = (nowPercentInt.toFloat() / 100.0).toFloat()
                addAllItem(mainResponse.plan)
                Log.e("$nowPercentFlt", "nowPercentFlt")
                Log.e((nowPercentInt.toFloat() / 10.0).toString(), "nowPercentt")
                stateIdx = it.result.stateIdx
            } else {

            }
        }, {
            it.printStackTrace()
        }).disposeOnDestroy(this)

    fun initImg(classArmy: Int): Int {
        return when(classArmy){
            0->  R.drawable.icon_class_1
            1-> R.drawable.icon_class_2
            2->   R.drawable.icon_class_3
            3->   R.drawable.icon_class_4
            else-> R.drawable.icon_class_1
        }
    }
    fun initTodayDate(){
        val df = SimpleDateFormat("MM월 dd일")
        val calToday = Calendar.getInstance(TimeZone.getDefault())
        todayDate.value= calToday.get(Calendar.MONTH).plus(1).toString()+"월 "+calToday.get(Calendar.DATE)+"일"
    }

    @SuppressLint("SimpleDateFormat")
    fun dateFormat(inputDate: String): String {
        val dfParse = SimpleDateFormat("yyyy-MM-dd")
        val dfFormat = SimpleDateFormat("yyyy.MM.dd")

        return dfFormat.format(dfParse.parse(inputDate))
    }

    fun initNextPromDay(nowClass: Int): Int {
        return when (nowClass) {
            0 -> calDday(mainResponse.strPrivate)
            1 -> calDday(mainResponse.strCorporal)
            2 -> calDday(mainResponse.strSergeant)
            3 -> calDday(mainResponse.endDate)
            else -> 0
        }
    }

    fun initClass(serviceId: Int, nowClass: Int): String {
        // if(serviceId == 0){
        return when (nowClass) {
            0 -> "일병"
            1 -> "이병"
            2 -> "상병"
            3 -> "병장"
            else -> ""
        }
        //}
//        else{
//            return ""
//        }
    }

    fun initNextProm(nowClass: Int): String {
        return when (nowClass) {
            0 -> "이병"
            1 -> "상병"
            2 -> "병장"
            3 -> "전역"
            else -> ""
        }
    }

    //Main 일정 recyclerview itemlist
    val liveMainPlan = MutableLiveData<ArrayList<Main.Result.PlanMain>>().apply {
        this.postValue(
            arrayListOf(
                Main.Result.PlanMain(0, "")
            )
        )
    }
    var planItems = ArrayList<Main.Result.PlanMain>()

    //Main 일정 itemMethod
    fun addItem(item: Main.Result.PlanMain) {

        if (planItems.size == 0) {
            planItems.add(0, Main.Result.PlanMain(0, ""))
            //  planItems.add(item)
            liveMainPlan.value = planItems
        } else {
            planItems.add(item)
            liveMainPlan.value = planItems
        }
    }

    fun addAllItem(item: List<Main.Result.PlanMain>) {

        if (planItems.size == 0) {
            planItems.add(0, Main.Result.PlanMain(0, ""))
            //   planItems.add(item[0])
            liveMainPlan.value = planItems
        } else {
            planItems.addAll(item)
            liveMainPlan.value = planItems
        }
    }

    fun removeItem(item: List<Main.Result.PlanMain>, i: Int) {
        planItems.remove(item[i])
        liveMainPlan.value = planItems
    }

//    fun notifyChange() {
//        val items: ArrayList<MainSchedule>? = liveMainPlan.value
//        liveMainPlan.value = items
//    }


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

        return (nowDays * 100).div(allDays.toFloat())
    }

    fun calHobongDday(): Int {
        val cal = Calendar.getInstance(TimeZone.getDefault())
        val allMonthDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        Log.e(allMonthDay.toString(),"이번달 최대일")
        val today = cal.get(Calendar.DATE)

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

//    @BindingAdapter("setImageUrl")
//    fun setUserProfileImage(view: ImageView, url: String?) {
//        if (!userProfileImgUrl.isNullOrBlank()) {
//            Glide.with(view).load(url).circleCrop().placeholder(R.drawable.graphic_profile_big).into(view)
//        }
//    }

}
