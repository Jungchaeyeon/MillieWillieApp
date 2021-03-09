package com.makeus.milliewillie.ui.dDay.birthday

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayAnniversaryBinding
import com.makeus.milliewillie.databinding.FragmentDDayBirthdayBinding
import org.koin.android.viewmodel.ext.android.viewModel

class BirthdayFragment: BaseDataBindingFragment<FragmentDDayBirthdayBinding>(R.layout.fragment_d_day_birthday) {

    private val viewModel by viewModel<BirthdayViewModel>()

    override fun FragmentDDayBirthdayBinding.onBind() {
        vi = this@BirthdayFragment
        vm = viewModel
        viewModel.bindLifecycle(this@BirthdayFragment)
    }
}