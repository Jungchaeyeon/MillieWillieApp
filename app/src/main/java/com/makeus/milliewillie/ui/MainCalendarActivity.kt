package com.makeus.milliewillie.ui

import android.graphics.Color
import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.calendar.SelectionDecorator
import com.makeus.milliewillie.calendar.SundayDecorator
import com.makeus.milliewillie.databinding.ActivityMainCalendarviewBinding
import com.makeus.milliewillie.databinding.ItemMainScheduleBinding
import com.makeus.milliewillie.model.Main
import com.makeus.milliewillie.ui.decorators.CustomEventDecorator
import com.makeus.milliewillie.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter
import kotlinx.android.synthetic.main.activity_main_calendarview.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.item_home_layout.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainCalendarActivity :
    BaseDataBindingActivity<ActivityMainCalendarviewBinding>(R.layout.activity_main_calendarview) {

    val viewModel by viewModel<MainCalendarViewModel>()
    var dates = ArrayList<CalendarDay>()
    var date = CalendarDay.from(2021,1,1)
    var endDayCheck = ArrayList<Int>()
    var index =1F
    var bfIndex = 1.2F
    lateinit var mWidget : MaterialCalendarView

    companion object {
        fun getInstance() = MainCalendarActivity()
    }

    override fun ActivityMainCalendarviewBinding.onBind() {
        vm = viewModel
        mWidget = binding.calendar

        calendar.setTitleFormatter(DateFormatTitleFormatter(SimpleDateFormat("yyyy년 MM월",
            Locale.KOREA)))

        rv_cal_list.run {

            adapter = BaseDataBindingRecyclerViewAdapter<Main.Result.PlanMain>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Main.Result.PlanMain, ItemMainScheduleBinding>(
                        R.layout.item_main_schedule
                    ) {
                        if (this@MainCalendarActivity.viewModel.planItems.size >= 2) {
                            txtBlank.visibility = View.GONE
                        }
                        item = it
                    })

        }
        binding.calendar.apply {
            addDecorator(SundayDecorator())
            state().edit()
                .isCacheCalendarPositionEnabled(false)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
            isDynamicHeightEnabled = true
            setPadding(0, -10, 0, 0)
        }

        binding.calendar.setOnMonthChangedListener { widget, date ->
            if(date.month>=9) viewModel.month = date.year.toString() + date.month.plus(1).toString()
            else if(date.month>=1 || date.month<9) viewModel.month = date.year.toString() +"0"+ date.month.plus(
                1).toString()
            Log.e(viewModel.month.toString(), "month")
            dates.clear()
            getMainCalendar(widget)
        }

        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            Log.e("$date", "dateChangeListener")
            widget.addDecorator(SelectionDecorator(date, this@MainCalendarActivity))
        }

    }



    fun getMainCalendar(widget: MaterialCalendarView){
        viewModel.getMainCalendar { success->
            if(success) {

                val planCalendar = viewModel.planCalendar
                endDayCheck.clear()

                planCalendar.forEach {
                    index =1.1F
                    val df = SimpleDateFormat("yyyy-MM-dd")
                    val sDate = df.parse(it.startDate)
                    val sCal = Calendar.getInstance(TimeZone.getDefault())
                    sCal.time= sDate
                    val eDate = df.parse(it.endDate)
                    val eCal =Calendar.getInstance(TimeZone.getDefault())
                    eCal.time =eDate

                    endDayCheck.forEach {
                        if((it+1) >= sCal.get(Calendar.DATE)){
                            index += 0.25F
                        }
                    }

                    endDayCheck.add(eCal.get(Calendar.DATE))
                    Log.e(sCal.get(Calendar.DATE).toString(), "sDate??")
                    Log.e(index.toString(), "index")
                    Log.e(endDayCheck.toString(), "endDayCheck")
                    val eventDecorator : CustomEventDecorator
                    if(sCal.get(Calendar.DATE) == eCal.get(Calendar.DATE)){
                        //일정이 1일일 때
                        eventDecorator= CustomEventDecorator(Color.parseColor(it.color), dates,this,index,true)
                    }
                    else{
                        //일정이 2일 이상일 때
                        eventDecorator= CustomEventDecorator(Color.parseColor(it.color), dates,this,index,false)
                    }
                    eventDecorator.addFirstAndLast(CalendarDay.from(sCal.get(Calendar.YEAR), sCal.get(Calendar.MONTH), sCal.get(Calendar.DATE)),
                        CalendarDay.from(eCal.get(Calendar.YEAR), eCal.get(Calendar.MONTH), eCal.get(Calendar.DATE)))
                    mWidget.addDecorator(eventDecorator)

            }
            }
        }
    }


}