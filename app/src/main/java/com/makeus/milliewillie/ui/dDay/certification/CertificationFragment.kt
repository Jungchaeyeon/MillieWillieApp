package com.makeus.milliewillie.ui.dDay.certification

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayAnniversaryBinding
import com.makeus.milliewillie.databinding.FragmentDDayCertificationBinding
import com.makeus.milliewillie.ui.fragment.DatePickerBirthBottomSheetDialogFragment
import com.makeus.milliewillie.ui.fragment.DatePickerDdayBottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel

class CertificationFragment: BaseDataBindingFragment<FragmentDDayCertificationBinding>(R.layout.fragment_d_day_certification) {

    private val viewModel by viewModel<CertificationViewModel>()

    override fun FragmentDDayCertificationBinding.onBind() {
        vi = this@CertificationFragment
        vm = viewModel
        viewModel.bindLifecycle(this@CertificationFragment)
    }

    fun onClickDdayDate() {
        fragmentManager?.let {
            DatePickerDdayBottomSheetDialogFragment.getInstance()
                .setOnClickOk {date, gapDay ->
                    viewModel.liveDataToday.postValue(date)
                    viewModel.liveDataTodayInfo.postValue(gapDay)
                }.show(it)
        }
    }

    fun setOnClickLocation() {
        ActivityNavigator.with(this).map().start()
    }

}