package com.makeusteam.milliewillie.ui.utils

import android.annotation.SuppressLint
import com.makeusteam.milliewillie.model.main.PromotionState
import com.makeusteam.milliewillie.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class ArmyInfoCalculationUtils {

    companion object {

        @SuppressLint("SimpleDateFormat")
        fun totalDdayCalculation(date1: String, date2: String): Int { //총 복무일 - 복무일 percent의 분모역할
            var calDateDays = 0L
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            try {
                val firstDate: Long? = dateFormat.parse(date1)?.time
                val secondDate: Long? = dateFormat.parse(date2)?.time

                if (firstDate != null && secondDate != null) {
                    val calDate: Long = firstDate - secondDate
                    calDateDays = calDate / (24 * 60 * 60 * 1000) //일 단위 계산
                    calDateDays = abs(calDateDays)
                }
            } catch (e: ParseException) {
                Log.e("$calDateDays")
            }
            return calDateDays.toInt()
        }

        @SuppressLint("SimpleDateFormat")
        fun dDayCalculation(inputDate: String?): Int {
            var calDateDays: Long = 0L
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            try {
                inputDate?.let {
                    val todayDate : Date= Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul")).time
                    val inputDate : Date = dateFormat.parse(inputDate) ?: todayDate


                    val difference = inputDate.time - todayDate.time

                    calDateDays = difference / (24 * 60 * 60 * 1000)
                    calDateDays = abs(calDateDays)
                }

            } catch (e: ParseException) {
                Log.e("dDayCalculation : $e")
            }
            return calDateDays.toInt()
        }

        fun hobongDdayCalculation(): Int {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            val allMonthDay =
                Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)//이번달 최대일
            val today = cal.get(Calendar.DATE) //오늘
            return allMonthDay - today
        }

        @SuppressLint("SimpleDateFormat")
        fun serviceTimePercent(date1: String, date2: String): Float = with(ArmyInfoCalculationUtils){ //전열일 대비 복무기간 퍼센트
            //percent
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val nowFormat = dateFormat.format(cal.time).toString() //오늘날짜

            val allDays = totalDdayCalculation(date1, date2) //입대 ~ 전역
            val nowDays = totalDdayCalculation(date1, nowFormat) // 입대 ~ 오늘

            return (nowDays * 100).div(allDays.toFloat())
        }

    }


}