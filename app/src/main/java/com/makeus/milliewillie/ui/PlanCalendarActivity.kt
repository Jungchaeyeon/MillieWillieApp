package com.makeus.milliewillie.ui


import com.andrewjapar.rangedatepicker.CalendarPicker
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPlanCalendarBinding
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_plan_calendar.*
import java.util.*

class PlanCalendarActivity :
    BaseDataBindingActivity<ActivityPlanCalendarBinding>(R.layout.activity_plan_calendar) {

    override fun ActivityPlanCalendarBinding.onBind() {
        vi = this@PlanCalendarActivity

        val firstCalendarDate = Calendar.getInstance()
        firstCalendarDate.set(2021, 3, 11)

        val secondCalendarDate = Calendar.getInstance()
        secondCalendarDate.time = firstCalendarDate.time
        secondCalendarDate.add(Calendar.YEAR, 1)

        val thirdCalendarDate = Calendar.getInstance()
        thirdCalendarDate.time = firstCalendarDate.time
        thirdCalendarDate.add(Calendar.MONTH, 0)

        calendar_view.setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
            departure_date.text = startLabel
            return_date.text = endLabel
        }
        calendar_view.setOnStartSelectedListener { startDate, label ->
            departure_date.text = label
            return_date.text = "-"
        }
    }
}