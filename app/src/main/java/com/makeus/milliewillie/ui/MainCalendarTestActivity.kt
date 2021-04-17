package com.makeus.milliewillie.ui

import android.graphics.Color
import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.calendar.MainSelectionDecorator
import com.makeus.milliewillie.calendar.SundayDecorator
import com.makeus.milliewillie.databinding.ActivityMainCalendarviewBinding
import com.makeus.milliewillie.databinding.ItemMainCalScheduleBinding
import com.makeus.milliewillie.databinding.ItemMainCalTestScheduleBinding
import com.makeus.milliewillie.model.CalendarDayResponse
import com.makeus.milliewillie.model.Main
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.decorators.CustomEventDecorator
import com.makeus.milliewillie.util.Log
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

class MainCalendarTestActivity :
    BaseDataBindingActivity<ActivityMainCalendarviewBinding>(R.layout.activity_main_calendarview) {

    val viewModel by viewModel<MainCalendarViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    var today = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))
    var isDateList = ArrayList<CalendarDay>()
    lateinit var mWidget : MaterialCalendarView

    companion object {
        fun getInstance() = MainCalendarTestActivity()
    }
    fun onClickItem(item: CalendarDayResponse.Result.Plan){
        Log.e("클릭확인")
        repositoryCached.setValue(LocalKey.PLANID, item.planId.toString())
        ActivityNavigator.with(this).planoutput().start()
    }

    override fun ActivityMainCalendarviewBinding.onBind() {
        vm = viewModel

        calendar.setTitleFormatter(DateFormatTitleFormatter(SimpleDateFormat("yyyy년 MM월",
            Locale.KOREA)))

        rv_cal_list.isNestedScrollingEnabled = false
        rv_cal_list.run {
            adapter = BaseDataBindingRecyclerViewAdapter<CalendarDayResponse.Result.Plan>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<CalendarDayResponse.Result.Plan, ItemMainCalTestScheduleBinding>(
                        R.layout.item_main_cal_test_schedule
                    ) {
                        vi = this@MainCalendarTestActivity
                        item = it
                    })

        }
        binding.btnToday.setOnClickListener {

            mWidget.removeDecorators()
            mWidget.clearSelection()
            val oneDayDecorator = MainSelectionDecorator(CalendarDay.today(),this@MainCalendarTestActivity)
            mWidget.apply {
                addDecorators(SundayDecorator(),oneDayDecorator)
                invalidateDecorators()
            }
            getMainCalendar()
        }
        binding.btnBackMain.setOnClickListener {
            ActivityNavigator.with(this@MainCalendarTestActivity).main().start()
        }
        binding.calendar.apply {
            addDecorators(SundayDecorator())
            state().edit()
                .isCacheCalendarPositionEnabled(false)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
            isDynamicHeightEnabled = false
            setPadding(0, -10, 0, 0)
        }

        binding.calendar.setOnMonthChangedListener { widget, date ->
            Log.e("달력체인지확인")
            if(date.month>=9) viewModel.month = date.year.toString() + date.month.plus(1).toString()
            else if(date.month>=1 || date.month<9) viewModel.month = date.year.toString() +"0"+ date.month.plus(
                1).toString()
            widget.removeDecorators()
            widget.apply {
                addDecorators(SundayDecorator())
                invalidateDecorators()
            }
            getMainCalendar()
        }
        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            Log.e("$date", "dateChangeListener")
            viewModel.pickDay.value = date.month.plus(1).toString()+"월 "+date.day.toString()+"일"
            if(today.get(Calendar.DATE).equals(date.day)){
                binding.txtToday.visibility=View.VISIBLE
            }
            else{
                binding.txtToday.visibility=View.GONE
            }
            widget.setDateTextAppearance(R.style.spoqa_medium_12)
            widget.removeDecorators()
            val oneDayDecorator = MainSelectionDecorator(
                date,
                this@MainCalendarTestActivity
            )


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
        var index =1.0F
        viewModel.getMainCalendarDay {
            if(it && viewModel.planItems.size>0){
                binding.txtBlank.visibility=View.GONE
            }

            Log.e(viewModel.planDayCalendar.toString(),"확인1")
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

                if(index>1.8F){
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


    fun getMainCalendar(){
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
        getMainCalendar()
        viewModel.pickDay.value = today.get(Calendar.MONTH).plus(1).toString()+"월 "+today.get(Calendar.DATE).toString()+"일"
        viewModel.date = viewModel.month+today.get(Calendar.DATE).toString()
      //  getMainCalendarDay(mWidget)
        val oneDayDecorator = MainSelectionDecorator(CalendarDay.from(today),this@MainCalendarTestActivity)
        mWidget.apply {
            addDecorators(SundayDecorator(),oneDayDecorator)
            invalidateDecorators()
        }
    }
    }


