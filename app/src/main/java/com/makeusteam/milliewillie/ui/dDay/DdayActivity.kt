package com.makeusteam.milliewillie.ui.dDay

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityDDayBinding
import com.makeusteam.milliewillie.ui.dDay.anniversary.AnniversaryFragment
import com.makeusteam.milliewillie.ui.dDay.birthday.BirthdayFragment
import com.makeusteam.milliewillie.ui.dDay.certification.CertificationFragment
import com.makeusteam.milliewillie.ui.dDay.ncee.NceeFragment
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


    @RequiresApi(Build.VERSION_CODES.N)
    override fun ActivityDDayBinding.onBind() {
        vi = this@DdayActivity
        vm = viewModel
        viewModel.bindLifecycle(this@DdayActivity)

        replaceViewFrame(classificationValue)
        binding.dDayBtnAnni.isSelected = true

        btnAnni = binding.dDayBtnAnni
        btnBirthday = binding.dDayBtnBirthday
        btnCertification = binding.dDayBtnCertification
        btnNcee = binding.dDayBtnNcee


    }

    fun onClickDdayDate() {
        when {
            btnBirthday.isSelected -> {
                DatePickerBirthBottomSheetDialogFragment.getInstance()
                    .setOnClickOk {date, gapDay, lunarBirthday, thisYearLunarBirthday, nextYearLunarBirthday ->
                        viewModel.liveDataDayGap.postValue(gapDay)
//                        val dateForm = "$date\n$lunarBirthday\n$thisYearLunarBirthday\n$nextYearLunarBirthday"
                        viewModel.liveDataDdayDate.postValue(date)
//                        Log.e(dateForm)
//                        Log.e(viewModel.liveDataDdayDate.value!!.toString())
                    }.show(supportFragmentManager)
            }
            else -> {
                DatePickerDdayBottomSheetDialogFragment.getInstance()
                    .setOnClickOk {date, gapDay ->
                        viewModel.liveDataDayGap.postValue(gapDay)
                        viewModel.liveDataDdayDate.postValue(date)
                    }.show(supportFragmentManager)
            }
        }

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