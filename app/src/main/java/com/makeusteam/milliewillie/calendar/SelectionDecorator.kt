package com.makeusteam.milliewillie.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresApi
import com.makeusteam.milliewillie.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

@SuppressLint("UseCompatLoadingForDrawables")
class SelectionDecorator(private val date: CalendarDay, context: Context) :
    DayViewDecorator {

    private val drawable: Drawable = context.resources.getDrawable(R.drawable.calendar_item_background)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return date == day
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable)
    }

}