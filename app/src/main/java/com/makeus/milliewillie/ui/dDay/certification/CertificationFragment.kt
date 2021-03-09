package com.makeus.milliewillie.ui.dDay.certification

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayAnniversaryBinding
import com.makeus.milliewillie.databinding.FragmentDDayCertificationBinding
import org.koin.android.viewmodel.ext.android.viewModel

class CertificationFragment: BaseDataBindingFragment<FragmentDDayCertificationBinding>(R.layout.fragment_d_day_certification) {

    private val viewModel by viewModel<CertificationViewModel>()

    override fun FragmentDDayCertificationBinding.onBind() {
        vi = this@CertificationFragment
        vm = viewModel
        viewModel.bindLifecycle(this@CertificationFragment)
    }

}