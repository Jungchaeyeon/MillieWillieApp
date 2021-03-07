package com.makeus.milliewillie.ui.dDay.anniversary

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayAnniversaryBinding

class AnniversaryFragment: BaseDataBindingFragment<FragmentDDayAnniversaryBinding>(R.layout.fragment_d_day_anniversary) {

    override fun FragmentDDayAnniversaryBinding.onBind() {
        vi = this@AnniversaryFragment
    }
}