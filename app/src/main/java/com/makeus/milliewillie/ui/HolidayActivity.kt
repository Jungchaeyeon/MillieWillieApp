package com.makeus.milliewillie.ui

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityHolidayBinding
import com.makeus.milliewillie.ui.plan.MakePlanViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HolidayActivity :
    BaseDataBindingActivity<ActivityHolidayBinding>(R.layout.activity_holiday) {

    val viewModel by viewModel<MakePlanViewModel>()

    companion object {
        fun getInstance() = HolidayActivity()
    }

    override fun ActivityHolidayBinding.onBind() {
        vm = viewModel
        btnX.setPadding(10,10,10,10)
    }


}