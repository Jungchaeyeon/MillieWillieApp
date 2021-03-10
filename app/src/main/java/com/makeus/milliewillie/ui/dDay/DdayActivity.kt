package com.makeus.milliewillie.ui.dDay

import android.view.View
import android.widget.Toast
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityDDayBinding
import com.makeus.milliewillie.ui.dDay.anniversary.AnniversaryFragment
import com.makeus.milliewillie.ui.dDay.birthday.BirthdayFragment
import com.makeus.milliewillie.ui.dDay.certification.CertificationFragment
import com.makeus.milliewillie.ui.dDay.ncee.NceeFragment
import com.makeus.milliewillie.ui.fragment.DatePickerBasicBottomSheetDialogFragment
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

    lateinit var btnAnni: View
    lateinit var btnBirthday: View
    lateinit var btnCertification: View
    lateinit var btnNcee: View


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

        binding.dDayAnniTextComplete.setOnClickListener{
            Toast.makeText(this@DdayActivity, "Complete", Toast.LENGTH_SHORT).show()
        }


    }

    fun onClickDdayDate() {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDataDdayDate.postValue(it)
            }.show(supportFragmentManager)
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