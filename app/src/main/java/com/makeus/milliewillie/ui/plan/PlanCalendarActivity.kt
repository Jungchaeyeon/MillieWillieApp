package com.makeus.milliewillie.ui.plan


import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPlanCalendarBinding
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

class PlanCalendarActivity :
    BaseDataBindingActivity<ActivityPlanCalendarBinding>(R.layout.activity_plan_calendar) {

    val viewModel by viewModel<MakePlanViewModel>()

    val repositoryCached by inject<RepositoryCached>()
    private var dayNightStr: String = ""
    private var dayAvail: String = ""
    var startStrDate: String = ""
    var endStrDate: String = ""
    val firstCalendarDate = Calendar.getInstance(Locale.KOREA)
    val thirdCalendarDate = Calendar.getInstance()

    companion object {
        fun getInstance() = PlanCalendarActivity()
    }
    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.plansRequest =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as PlansRequest
    }

    override fun ActivityPlanCalendarBinding.onBind() {
        vi = this@PlanCalendarActivity
        vm = viewModel
        viewModel.bindLifecycle(this@PlanCalendarActivity)

        //val firstCalendarDate = Calendar.getInstance(Locale.KOREA)
        val dateFormat = SimpleDateFormat("MM월 dd일 (EE)")

        val secondCalendarDate = Calendar.getInstance()
        secondCalendarDate.time = firstCalendarDate.time
        secondCalendarDate.add(Calendar.YEAR, 1)

        //val thirdCalendarDate = Calendar.getInstance()
        thirdCalendarDate.time = firstCalendarDate.time
        thirdCalendarDate.add(Calendar.MONTH, 0)

        calendar_view.setOnRangeSelectedListener { startDate, endDate, startLabel, endLabel ->
            startStrDate = dateFormat.format(startDate)
            endStrDate = dateFormat.format(endDate)



            val calcuDate = (endDate.time - startDate.time) / (60 * 60 * 24 * 1000)

            //몇박 몇일
            dayNightStr = "$calcuDate" + "박${calcuDate + 1}일"
            viewModel.liveDate.postValue("$startStrDate - $endStrDate")

            //휴가 사용 가능일 수
            dayAvail= calcuDate.toInt().plus(1).toString()


            repositoryCached.setValue(LocalKey.PLANSTART, planDateChange(startDate))
            repositoryCached.setValue(LocalKey.PLANEND,planDateChange(endDate))
            repositoryCached.setValue(LocalKey.ONLYDAY,dayNightStr)
            repositoryCached.setValue(LocalKey.DAYNIGHT,dayNightStr)
        }

        calendar_view.setOnStartSelectedListener { startDate, label ->
            startStrDate = dateFormat.format(startDate)

            viewModel.liveDate.postValue("$startStrDate")

            repositoryCached.setValue(LocalKey.PLANSTART, planDateChange(startDate))
            repositoryCached.setValue(LocalKey.PLANEND, planDateChange(startDate))
            repositoryCached.setValue(LocalKey.ONLYDAY,"1일")
            repositoryCached.setValue(LocalKey.DAYNIGHT,"1일")
        }

        calendar_view.apply {
            setRangeDate(firstCalendarDate.time, secondCalendarDate.time)
            setSelectionDate(firstCalendarDate.time, thirdCalendarDate.time)
        }

    }


    fun onClickBack() {
        viewModel.liveDayAndNight.postValue(dayNightStr)
        repositoryCached.setValue(LocalKey.AVAILHOLI,dayAvail)
        repositoryCached.setValue(LocalKey.PICKDATE,viewModel.liveDate.value.toString())
       //Log.e(repositoryCached.getPickDate().toString(),"pickDate")

        onBackPressed()
    }

    fun planDateChange(date: Date): String {
        val planDateFormat = SimpleDateFormat("yyyy-MM-dd")
        //Log.e(planDateFormat.format(date).toString(),"날짜로그출력")
        return planDateFormat.format(date).toString()
    }

    override fun onResume() {
        super.onResume()
    }
}