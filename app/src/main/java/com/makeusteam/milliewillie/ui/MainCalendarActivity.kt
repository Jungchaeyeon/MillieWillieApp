package com.makeusteam.milliewillie.ui

import android.graphics.Color
import android.view.View
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.calendar.MainSelectionDecorator
import com.makeusteam.milliewillie.calendar.SundayDecorator
import com.makeusteam.milliewillie.databinding.ActivityMainCalendarviewBinding
import com.makeusteam.milliewillie.databinding.ItemMainCalScheduleBinding
import com.makeusteam.milliewillie.model.CalendarDayResponse
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.decorators.CustomEventDecorator
import com.makeusteam.milliewillie.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.format.DateFormatTitleFormatter
import kotlinx.android.synthetic.main.activity_main_calendarview.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.item_home_layout.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainCalendarActivity :
    BaseDataBindingActivity<ActivityMainCalendarviewBinding>(R.layout.activity_main_calendarview) {

    val viewModel by viewModel<MainCalendarViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    var today = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    var isDateList = ArrayList<CalendarDay>()
    lateinit var mWidget : MaterialCalendarView

    companion object {
        fun getInstance() = MainCalendarActivity()
    }
    fun onClickItem(item: CalendarDayResponse.Result.Plan){
        Log.e("????????????")
        repositoryCached.setValue(LocalKey.PLANID, item.planId.toString())
        ActivityNavigator.with(this).planoutput().start()
    }

    override fun ActivityMainCalendarviewBinding.onBind() {
        vm = viewModel

        calendar.setTitleFormatter(DateFormatTitleFormatter(SimpleDateFormat("yyyy??? MM???",
            Locale.KOREA)))

        rv_cal_list.run {
            adapter = BaseDataBindingRecyclerViewAdapter<CalendarDayResponse.Result.Plan>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<CalendarDayResponse.Result.Plan, ItemMainCalScheduleBinding>(
                        R.layout.item_main_cal_schedule
                    ) {
                        vi = this@MainCalendarActivity
                        item = it
                    })

        }
        binding.calendar.apply {
            addDecorators(SundayDecorator())
            state().edit()
                .isCacheCalendarPositionEnabled(false)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
            isDynamicHeightEnabled = true
            setPadding(0, -10, 0, 0)
        }

        binding.calendar.setOnMonthChangedListener { widget, date ->
            Log.e("?????????????????????")
            if(date.month>=9) viewModel.month = date.year.toString() + date.month.plus(1).toString()
            else if(date.month>=1 || date.month<9) viewModel.month = date.year.toString() +"0"+ date.month.plus(
                1).toString()
            widget.removeDecorators()
            widget.apply {
                addDecorators(SundayDecorator())
                invalidateDecorators()
            }
            Log.e(viewModel.month.toString(), "month")
            getMainCalendar(widget)
        }

        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            Log.e("$date", "dateChangeListener")
            viewModel.pickDay.value = date.month.plus(1).toString()+"??? "+date.day.toString()+"???"
            if(today.get(Calendar.DATE).equals(date.day)){
                binding.txtToday.visibility=View.VISIBLE
            }
            else{
                binding.txtToday.visibility=View.GONE
            }
            widget.setDateTextAppearance(R.style.spoqa_medium_12)
            widget.removeDecorators()
            val oneDayDecorator = MainSelectionDecorator(date,this@MainCalendarActivity)


            if(date.day <10){
                viewModel.date = viewModel.month+"0"+ date.day.toString()}
            else{
                viewModel.date = viewModel.month+ date.day.toString()
            }

            getMainCalendarDay(mWidget,date)

            widget.apply {
                addDecorators(SundayDecorator(),oneDayDecorator)
                invalidateDecorators()
            }
        }

    }

    fun getMainCalendarDay(widget: MaterialCalendarView, date : CalendarDay){
        viewModel.planItems.clear()
        var index =1.2F
        viewModel.getMainCalendarDay {
            if(it && viewModel.planItems.size>0){
                binding.txtBlank.visibility=View.GONE
            }

            Log.e(viewModel.planDayCalendar.toString(),"??????1")
            val planDayCalendar = viewModel.planDayCalendar
            planDayCalendar.forEach { plan->

                val df = SimpleDateFormat("yyyy-MM-dd")
                val sDate = df.parse(plan.startDate)
                val sCal = Calendar.getInstance(TimeZone.getDefault())
                sCal.time = sDate
                val eDate = df.parse(plan.endDate)
                val eCal = Calendar.getInstance(TimeZone.getDefault())
                eCal.time = eDate

                        index += 0.21F


                val eventDecorator : CustomEventDecorator = if(sDate.equals(eDate)){
                    CustomEventDecorator(Color.parseColor(plan.color), isDateList, this, index, true)
                } else{
                    CustomEventDecorator(Color.parseColor(plan.color), isDateList, this, index, false)
                }

                if(index>2.0F){
                    eventDecorator.addFirstAndLast(CalendarDay.from(date.year, date.month,date.day),
                        CalendarDay.from(date.year, date.month, date.day))
                }
                else {
                    eventDecorator.addFirstAndLast(CalendarDay.from(sCal.get(Calendar.YEAR),
                        sCal.get(Calendar.MONTH), sCal.get(Calendar.DATE)),
                        CalendarDay.from(eCal.get(Calendar.YEAR),
                            eCal.get(Calendar.MONTH),
                            eCal.get(
                                Calendar.DATE)))
                }
                binding.calendar.apply {
                    addDecorators(SundayDecorator(),eventDecorator)
                    invalidateDecorators()
                }
            }
        }
    }


    fun getMainCalendar(widget: MaterialCalendarView){
        viewModel.getMainCalendar { success->
            if(success) {
                val planCalendar = viewModel.planCalendar
                planCalendar.forEach {

                    val df = SimpleDateFormat("yyyy-MM-dd")
                    val sDate = df.parse(it.startDate)
                    val sCal = Calendar.getInstance(TimeZone.getDefault())
                    sCal.time = sDate
                    val eDate = df.parse(it.endDate)
                    val eCal = Calendar.getInstance(TimeZone.getDefault())
                    eCal.time = eDate

                    val eventDecorator = CustomEventDecorator(Color.parseColor("#dddddd"), isDateList, this, 1.2F, true)

                    eventDecorator.addFirstAndLast(CalendarDay.from(sCal.get(Calendar.YEAR),
                        sCal.get(Calendar.MONTH), sCal.get(Calendar.DATE)),
                        CalendarDay.from(eCal.get(Calendar.YEAR), eCal.get(Calendar.MONTH), eCal.get(
                            Calendar.DATE)))
                    binding.calendar.apply {
                        addDecorators(SundayDecorator(),eventDecorator)
                        invalidateDecorators()
                    }
                }
                }
            }
        }


    override fun onResume() {
        super.onResume()
        mWidget = binding.calendar
        getMainCalendar(mWidget)
        viewModel.pickDay.value = today.get(Calendar.MONTH).plus(1).toString()+"??? "+today.get(Calendar.DATE).toString()+"???"
        viewModel.date = viewModel.month+today.get(Calendar.DATE).toString()
      //  getMainCalendarDay(mWidget)
        val oneDayDecorator = MainSelectionDecorator(CalendarDay.from(today),this@MainCalendarActivity)
        mWidget.apply {
            addDecorators(SundayDecorator(),oneDayDecorator)
            invalidateDecorators()
        }
    }
    }


