package com.makeus.milliewillie.util

object BasicTextFormat {
    fun BasicDashFormat(year: String, month: String, day: String): String{
        var monthText = ""
        monthText = if (month.length < 2) "0$month" else month

        return "$year-$monthText-$day"
    }

    fun BasicRoutineOptionTextForm(setCount: String, weight: String, count:String, time: String, index: Int): String {
        var textForm = ""
        var textWeight = ""
        var textCount = ""
        var textTime = ""


        if (weight != "-1") textWeight = weight
        if (count != "-1") textCount = count
        if (time != "-1") textTime = time

        if (index == 0) textForm = "$setCount $textWeight $textCount $textTime"
        else textForm = "\n${setCount} $textWeight $textCount $textTime"

        return textForm

    }

    fun BasicTotalTimeFormat(hour: String, min: String, sec: String): String {
        var textHour = ""
        var textMin = ""
        var textSec = ""

        textHour += if (hour.length < 2) "0$hour" else hour
        textMin += if (min.length < 2) "0$min" else min
        textSec += if (sec.length < 2) "0$sec" else sec

        return "$textHour : $textMin : $textSec"
    }

}