package com.makeusteam.milliewillie.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

@SuppressLint("UseCompatLoadingForDrawables")
class DotDecorator(private val color: Int, dates: Collection<CalendarDay>, context: Context) :
    DayViewDecorator {

    private val drawable: Drawable = context.resources.getDrawable(R.drawable.calendar_item_background)
    private val dates: HashSet<CalendarDay> = HashSet(dates)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun decorate(view: DayViewFacade) {
//        view.setBackgroundDrawable(drawable)
        Log.e(color.toString())
        view.addSpan(DotSpan(10f, color)); // 날자밑에 점
    }

}