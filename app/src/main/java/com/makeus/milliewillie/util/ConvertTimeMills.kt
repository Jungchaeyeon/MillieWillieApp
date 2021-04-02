package com.makeus.milliewillie.util

import java.util.*

object ConvertTimeMills {
    fun ConvertDateToMillis(year: Int, month: Int, day: Int): Long {
        val dateTimeInMillis = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }.timeInMillis

        return getIgnoredTimeDays(dateTimeInMillis)
    }

    private fun getIgnoredTimeDays(time: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = time

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

}