package com.makeusteam.milliewillie.ui.dDay.anniversary

import com.makeusteam.base.fragment.BaseDataBindingFragment
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.FragmentDDayAnniversaryBinding

class AnniversaryFragment: BaseDataBindingFragment<FragmentDDayAnniversaryBinding>(R.layout.fragment_d_day_anniversary) {

    override fun FragmentDDayAnniversaryBinding.onBind() {
        vi = this@AnniversaryFragment
    }
}