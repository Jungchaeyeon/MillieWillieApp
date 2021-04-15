package com.makeus.milliewillie.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.annotation.RequiresApi
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.R
import com.makeus.milliewillie.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.spans.DotSpan

@SuppressLint("UseCompatLoadingForDrawables")
class MainSelectionDecorator(private val date: CalendarDay, context: Context) :
    DayViewDecorator {

    private val drawable: Drawable = context.resources.getDrawable(R.drawable.calendar_main_item_bg)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return date==day

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(drawable)
        view.addSpan(ForegroundColorSpan(Color.parseColor("#00d8ff")))
    }

}