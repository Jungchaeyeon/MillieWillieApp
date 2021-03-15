package com.makeus.milliewillie.ui.plan


import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPlanCalendarBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_plan_calendar.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlanCalendarActivity :
    BaseDataBindingActivity<ActivityPlanCalendarBinding>(R.layout.activity_plan_calendar) {

    val viewModel by viewModel<MakePlanViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    override fun ActivityPlanCalendarBinding.onBind() {
        vi = this@PlanCalendarActivity
        vm = viewModel
        viewModel.bindLifecycle(this@PlanCalendarActivity)

        val firstCalendarDate = Calendar.getInstance(Locale.KOREA)
        val dateFormat = SimpleDateFormat("yyyy년 MM월 dd일")

        val secondCalendarDate = Calendar.getInstance()
        secondCalendarDate.time = firstCalendarDate.time
        secondCalendarDate.add(Calendar.YEAR, 1)

        val thirdCalendarDate = Calendar.getInstance()
        thirdCalendarDate.time = firstCalendarDate.time

        calendar_view.setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
            departure_date.text = dateFormat.format(startDate)
            return_date.text = dateFormat.format(endDate)
            val calcuDate = (startDate.time - endDate.time) / (60 * 60 * 24 * 1000)

        }

        calendar_view.setOnStartSelectedListener { startDate, label ->
            departure_date.text = dateFormat.format(startDate)

        }

        calendar_view.apply {
            setRangeDate(firstCalendarDate.time, secondCalendarDate.time)
            setSelectionDate(firstCalendarDate.time, thirdCalendarDate.time)
        }
    }
}