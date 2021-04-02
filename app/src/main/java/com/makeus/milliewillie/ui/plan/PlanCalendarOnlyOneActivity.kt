package com.makeus.milliewillie.ui.plan


import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPlanCalendarBinding
import com.makeus.milliewillie.databinding.ActivityPlanCalendarOnlyOneBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.activity_plan_calendar.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PlanCalendarOnlyOneActivity :
    BaseDataBindingActivity<ActivityPlanCalendarOnlyOneBinding>(R.layout.activity_plan_calendar_only_one) {

    val viewModel by viewModel<MakePlanViewModel>()

    val repositoryCached by inject<RepositoryCached>()
    private var dayNightStr: String = ""
    private var dayAvail: String = ""
    var startStrDate: String = ""
    var endStrDate: String = ""
    val firstCalendarDate = Calendar.getInstance(Locale.KOREA)
    val thirdCalendarDate = Calendar.getInstance()

    companion object {
        fun getInstance() = PlanCalendarOnlyOneActivity()
    }
    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.plansRequest =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as PlansRequest
    }

    override fun ActivityPlanCalendarOnlyOneBinding.onBind() {
        vi = this@PlanCalendarOnlyOneActivity
        vm = viewModel
        viewModel.bindLifecycle(this@PlanCalendarOnlyOneActivity)

        //val firstCalendarDate = Calendar.getInstance(Locale.KOREA)
        val dateFormat = SimpleDateFormat("MM월 dd일 (EE)")

        val secondCalendarDate = Calendar.getInstance()
        secondCalendarDate.time = firstCalendarDate.time
        secondCalendarDate.add(Calendar.YEAR, 1)

        //val thirdCalendarDate = Calendar.getInstance()
        thirdCalendarDate.time = firstCalendarDate.time
        thirdCalendarDate.add(Calendar.MONTH, 0)


        calendar_view.setOnStartSelectedListener { startDate, label ->
            startStrDate = dateFormat.format(startDate)

            viewModel.liveDate.postValue("$startStrDate")

            repositoryCached.setValue(LocalKey.PLANSTART, planDateChange(startDate))
            repositoryCached.setValue(LocalKey.PLANEND, planDateChange(startDate))
            repositoryCached.setValue(LocalKey.ONLYDAY,"1일")
            repositoryCached.setValue(LocalKey.DAYNIGHT,"1일")
        }


        calendar_view.apply {
          //  setRangeDate(firstCalendarDate.time, secondCalendarDate.time)
            setSelectionDate(firstCalendarDate.time, thirdCalendarDate.time)
        }

    }


    fun onClickBack() {
        viewModel.liveDayAndNight.postValue(dayNightStr)
        repositoryCached.setValue(LocalKey.PICKDATE,viewModel.liveDate.value.toString())

        onBackPressed()
    }

    fun planDateChange(date: Date): String {
        val planDateFormat = SimpleDateFormat("yyyy-MM-dd")
        return planDateFormat.format(date).toString()
    }

    override fun onResume() {
        super.onResume()
    }
}