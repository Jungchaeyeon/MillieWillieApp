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

class MainCalendarActivity :
    BaseDataBindingActivity<ActivityMainCalendarviewBinding>(R.layout.activity_main_calendarview) {

    val viewModel by viewModel<MainCalendarViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    //var date = CalendarDay.from(2021, 1, 1)
    var today = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"))

    var isDateList = ArrayList<CalendarDay>()
    lateinit var mWidget : MaterialCalendarView

    companion object {
        fun getInstance() = MainCalendarActivity()
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
            if(date.month>=9) viewModel.month = date.year.toString() + date.month.plus(1).toString()
            else if(date.month>=1 || date.month<9) viewModel.month = date.year.toString() +"0"+ date.month.plus(
                1).toString()
            Log.e(viewModel.month.toString(), "month")
            getMainCalendar(widget)
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
            val oneDayDecorator = MainSelectionDecorator(date,this@MainCalendarActivity)


            if(date.day <10){
                viewModel.date = viewModel.month+"0"+ date.day.toString()}
            else{
                viewModel.date = viewModel.month+ date.day.toString()
            }

            getMainCalendarDay(mWidget)

            widget.apply {
                addDecorators(SundayDecorator(),oneDayDecorator)
                invalidateDecorators()
            }

           // widget.setBackgroundResource(R.drawable.calendar_item_background)
           // widget.addDecorator(MainSelectionDecorator(date, this@MainCalendarActivity))
//            binding.calendar.apply {
//                addDecorators(MainSelectionDecorator(date, context!!))
//                invalidateDecorators()
//            }
        }

    }
    fun getMainCalendarDay(widget: MaterialCalendarView){
        viewModel.planItems.clear()
        viewModel.getMainCalendarDay {
            if(it && viewModel.planItems.size>0){
                binding.txtBlank.visibility=View.GONE
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
        viewModel.pickDay.value = today.get(Calendar.MONTH).plus(1).toString()+"월 "+today.get(Calendar.DATE).toString()+"일"
        viewModel.date = viewModel.month+today.get(Calendar.DATE).toString()
        getMainCalendarDay(mWidget)
        val oneDayDecorator = MainSelectionDecorator(CalendarDay.from(today),this@MainCalendarActivity)
        mWidget.apply {
            addDecorators(SundayDecorator(),oneDayDecorator)
            invalidateDecorators()
        }
    }
    }


