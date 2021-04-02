package com.makeus.milliewillie.ui.dDay.anniversary

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityDDayAnniversaryContentBinding
import com.makeus.milliewillie.repository.local.RepositoryCached
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class DdayOutputAnniversaryActivity: BaseDataBindingActivity<ActivityDDayAnniversaryContentBinding>(R.layout.activity_d_day_anniversary_content) {

    private val viewModel by viewModel<DdayOutputAnniversaryViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    override fun ActivityDDayAnniversaryContentBinding.onBind() {
        vi = this@DdayOutputAnniversaryActivity
        vm = viewModel
    }

    fun onClickModifyMenu() {

    }

    fun onClickDeleteMenu() {

    }

    fun onClickBack() {
        onBackPressed()
    }

}