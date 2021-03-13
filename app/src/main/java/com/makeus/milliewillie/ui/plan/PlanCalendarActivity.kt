package com.makeus.milliewillie.ui.plan


import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPlanCalendarBinding
import com.makeus.milliewillie.ext.showLongToastSafe
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_plan_calendar.*
import java.text.SimpleDateFormat
import java.util.*

class PlanCalendarActivity :
    BaseDataBindingActivity<ActivityPlanCalendarBinding>(R.layout.activity_plan_calendar) {

    override fun ActivityPlanCalendarBinding.onBind() {
        vi = this@PlanCalendarActivity

        val firstCalendarDate = Calendar.getInstance(Locale.KOREA)
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일 aa hh시 mm분 ss초")
        val date = dateFormat.format(firstCalendarDate.time)

        date.toString().showLongToastSafe()
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

        calendar_view.apply {
            setRangeDate(firstCalendarDate.time, secondCalendarDate.time)
            setSelectionDate(firstCalendarDate.time, thirdCalendarDate.time)
        }


    }
}