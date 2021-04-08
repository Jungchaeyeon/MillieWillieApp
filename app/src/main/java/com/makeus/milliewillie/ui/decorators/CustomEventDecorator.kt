package com.makeus.milliewillie.ui.decorators

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import com.makeus.milliewillie.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

@SuppressLint("UseCompatLoadingForDrawables")
class CustomEventDecorator(
    private val color: Int,
    dates: Collection<CalendarDay>,
    context: Context,
    index : Float,
    oneDay : Boolean
) :
    DayViewDecorator {
    var resources: Resources = context.resources
    private val dates: HashSet<CalendarDay> = HashSet(dates)
    private val mColor: Int = color
    private val mIndex: Float = index
    private val oneDay : Boolean = oneDay


    override fun shouldDecorate(day: CalendarDay): Boolean {
        return dates.contains(day)
    }

    override fun decorate(view: DayViewFacade) {
        if(oneDay){
            view.addSpan(CustomDotSpan(5F, mColor, mIndex,resources))
        }
        else{
            view.addSpan(CustomLineSpan(5F, mColor, mIndex,resources))
        }
    }

    fun addFirstAndLast(first: CalendarDay, last: CalendarDay) {
        for (i in 0 until last.day - first.day + 1) {
            // Log.e(String.valueOf(CalendarDay.from(first.getYear(),first.getMonth(),first.getDay()+i)),"확인할 값");
            dates.add(CalendarDay.from(first.year, first.month, first.day + i))
        }
    }
}