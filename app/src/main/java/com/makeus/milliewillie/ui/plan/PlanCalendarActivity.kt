package com.makeus.milliewillie.ui.plan


import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPlanCalendarBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.activity_plan_calendar.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlanCalendarActivity :
    BaseDataBindingActivity<ActivityPlanCalendarBinding>(R.layout.activity_plan_calendar) {

    val viewModel by viewModel<MakePlanViewModel>()

    val repositoryCached by inject<RepositoryCached>()
    private var dayNightStr: String = ""
    private var startStrDate: String = ""
    private var endStrDate: String = ""

    companion object {
        fun getInstance() = PlanCalendarActivity()
    }

    override fun ActivityPlanCalendarBinding.onBind() {
        vi = this@PlanCalendarActivity
        vm = viewModel
        viewModel.bindLifecycle(this@PlanCalendarActivity)

        val firstCalendarDate = Calendar.getInstance(Locale.KOREA)
        val dateFormat = SimpleDateFormat("MM월 dd일 (EE)")

        val secondCalendarDate = Calendar.getInstance()
        secondCalendarDate.time = firstCalendarDate.time
        secondCalendarDate.add(Calendar.YEAR, 1)

        val thirdCalendarDate = Calendar.getInstance()
        thirdCalendarDate.time = firstCalendarDate.time
        thirdCalendarDate.add(Calendar.MONTH, 0)

        calendar_view.setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
            startStrDate = dateFormat.format(startDate)
            endStrDate = dateFormat.format(endDate)
            val calcuDate = (endDate.time - startDate.time) / (60 * 60 * 24 * 1000)

            dayNightStr = "$calcuDate" + "박${calcuDate + 1}일"
            viewModel.liveDate.postValue("$startStrDate - $endStrDate")
        }

        calendar_view.setOnStartSelectedListener { startDate, label ->
            startStrDate = dateFormat.format(startDate)
            viewModel.liveDate.postValue("$startStrDate - $endStrDate")
        }

        calendar_view.apply {
            setRangeDate(firstCalendarDate.time, secondCalendarDate.time)
            setSelectionDate(firstCalendarDate.time, thirdCalendarDate.time)
        }

    }


    fun onClickBack() {
        viewModel.liveDayAndNight.postValue(dayNightStr)
        onBackPressed()
    }
}