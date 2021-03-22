package com.makeus.milliewillie.ui.dDay

import android.annotation.SuppressLint
import android.view.View
import android.widget.Toast
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityDDayBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.ScheduleRequest
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.dDay.anniversary.AnniversaryFragment
import com.makeus.milliewillie.ui.dDay.birthday.BirthdayFragment
import com.makeus.milliewillie.ui.dDay.certification.CertificationFragment
import com.makeus.milliewillie.ui.dDay.ncee.NceeFragment
import com.makeus.milliewillie.ui.fragment.DatePickerBirthBottomSheetDialogFragment
import com.makeus.milliewillie.ui.fragment.DatePickerDdayBottomSheetDialogFragment
import com.makeus.milliewillie.ui.login.LoginActivity.Companion.deviceToken
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

enum class Classification {
    ANNIVERSARY,
    BIRTHDAY,
    CERTIFICATION,
    NCEE
}

class DdayActivity: BaseDataBindingActivity<ActivityDDayBinding>(R.layout.activity_d_day) {

    private val viewModel by viewModel<DdayViewModel>()

    var classificationValue = Classification.ANNIVERSARY

    lateinit var btnAnni: View
    lateinit var btnBirthday: View
    lateinit var btnCertification: View
    lateinit var btnNcee: View


    override fun ActivityDDayBinding.onBind() {
        vi = this@DdayActivity
        vm = viewModel
        viewModel.bindLifecycle(this@DdayActivity)

        requestScheduleApi()

        replaceViewFrame(classificationValue)
        binding.dDayBtnAnni.isSelected = true

        btnAnni = binding.dDayBtnAnni
        btnBirthday = binding.dDayBtnBirthday
        btnCertification = binding.dDayBtnCertification
        btnNcee = binding.dDayBtnNcee

        binding.dDayAnniTextComplete.setOnClickListener{
            Toast.makeText(this@DdayActivity, "Complete", Toast.LENGTH_SHORT).show()
        }

    }

    fun onClickDdayDate() {
        when {
            btnBirthday.isSelected -> {
                DatePickerBirthBottomSheetDialogFragment.getInstance()
                    .setOnClickOk {date, gapDay ->
                        viewModel.liveDataDayGap.postValue(gapDay)
                        viewModel.liveDataDdayDate.postValue(date)
                    }.show(supportFragmentManager)
            }
            else -> {
                DatePickerDdayBottomSheetDialogFragment.getInstance()
                    .setOnClickOk {date, gapDay, year, month ->
                        viewModel.liveDataDayGap.postValue(gapDay)
                        viewModel.liveDataDdayDate.postValue(date)
                    }.show(supportFragmentManager)
            }
        }

    }

    @SuppressLint("CheckResult")
    fun requestScheduleApi() {
        val dummyData = ScheduleRequest(color = "빨간색", distinction = "일정", title = "토익 인강듣기", startDate = "2021-03-09", endDate = "2021-03-10", repetition = "월", push = "T", pushDeviceToken = deviceToken)

        viewModel.apiRepository.schedule(dummyData).subscribe({
            Log.e(it.isSuccess.toString())
            Log.e(it.message)
            Log.e(it.code.toString())
            Log.e(it.result.toString())
            "호출 성공".showShortToastSafe()
        },{
            Log.e(it.message.toString())
            "호출 실패".showShortToastSafe()
        }) .disposeOnDestroy(this)

//        viewModel.requestSchedule().subscribe() {
//            if (it.isSuccess) {
//                "호출 성공".showShortToastSafe()
//                Log.e(it.isSuccess.toString())
//                Log.e(it.message.toString())
//                Log.e(it.code.toString())
//                Log.e(it.result.toString())
//            } else {
//                "호출 실패".showShortToastSafe()
//                Log.e(it.isSuccess.toString())
//                Log.e(it.message.toString())
//                Log.e(it.code.toString())
//                Log.e(it.result.toString())
//            }
//        }.disposeOnDestroy(this)
    }

    fun setBtnStatus(position: Int){
        when (position) {
            1 -> {
                setBtnView(btnAnni)
                replaceViewFrame(Classification.ANNIVERSARY)
            }
            2 -> {
                setBtnView(btnBirthday)
                replaceViewFrame(Classification.BIRTHDAY)
            }
            3 -> {
                setBtnView(btnCertification)
                replaceViewFrame(Classification.CERTIFICATION)
            }
            4 -> {
                setBtnView(btnNcee)
                replaceViewFrame(Classification.NCEE)
            }
        }


    }

    fun setBtnView(btn: View) {

        val btnList = arrayListOf<View>(btnAnni, btnBirthday, btnCertification, btnNcee)

        if (!btn.isSelected) {
            btnList.forEach { view ->
                if (view == btn) btn.isSelected = !btn.isSelected
                else view.isSelected = false
            }
        }
    }


    fun replaceViewFrame(classification: Classification) {
        val fmbt = supportFragmentManager.beginTransaction()

        when (classification) {
            Classification.ANNIVERSARY -> {
                fmbt.replace(R.id.d_day_frame_layout, AnniversaryFragment()).commitAllowingStateLoss()
            }
            Classification.BIRTHDAY -> {
                fmbt.replace(R.id.d_day_frame_layout, BirthdayFragment()).commitAllowingStateLoss()
            }
            Classification.CERTIFICATION -> {
                fmbt.replace(R.id.d_day_frame_layout, CertificationFragment()).commitAllowingStateLoss()
            }
            Classification.NCEE -> {
                fmbt.replace(R.id.d_day_frame_layout, NceeFragment()).commitAllowingStateLoss()
            }

        }

    }

}