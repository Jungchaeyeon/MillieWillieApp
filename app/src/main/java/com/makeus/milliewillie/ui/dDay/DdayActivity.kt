package com.makeus.milliewillie.ui.dDay

import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityDDayBinding
import com.makeus.milliewillie.ui.dDay.anniversary.AnniversaryFragment
import com.makeus.milliewillie.ui.dDay.birthday.BirthdayFragment
import com.makeus.milliewillie.ui.dDay.certification.CertificationFragment
import com.makeus.milliewillie.ui.dDay.ncee.NceeFragment
import org.koin.android.viewmodel.ext.android.viewModel

enum class Classification {
    ANNIVERSARY,
    BIRTHDAY,
    CERTIFICATION,
    NCEE
}

class DdayActivity: BaseDataBindingActivity<ActivityDDayBinding>(R.layout.activity_d_day) {

    private val viewModel by viewModel<DdayViewModel>()

    var classificationValue = Classification.ANNIVERSARY

    override fun ActivityDDayBinding.onBind() {
        vi = this@DdayActivity
        vm = viewModel
        viewModel.bindLifecycle(this@DdayActivity)

        replaceViewFrame(classificationValue)
        setBtnStatus(binding.dDayBtnAnni)

        binding.dDayBtnAnni.setOnClickListener {
            classificationValue = Classification.ANNIVERSARY
            setBtnStatus(it)
            replaceViewFrame(classificationValue)
        }
        binding.dDayBtnBirthday.setOnClickListener {
            classificationValue = Classification.BIRTHDAY
            setBtnStatus(it)
            replaceViewFrame(classificationValue)
        }
        binding.dDayBtnCertification.setOnClickListener {
            classificationValue = Classification.CERTIFICATION
            setBtnStatus(it)
            replaceViewFrame(classificationValue)
        }
        binding.dDayBtnNcee.setOnClickListener {
            classificationValue = Classification.NCEE
            setBtnStatus(it)
            replaceViewFrame(classificationValue)
        }


    }

    fun setBtnStatus(btn: View) {
        val btnAnni = binding.dDayBtnAnni
        val btnBirthday = binding.dDayBtnBirthday
        val btnCertification = binding.dDayBtnCertification
        val btnNcee = binding.dDayBtnNcee
        val btnList = arrayListOf<View>(btnAnni, btnBirthday, btnCertification, btnNcee)

        btnList.forEach {view ->
            if (view == btn && !btn.isSelected) btn.isSelected = !btn.isSelected
            else view.isSelected = false
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