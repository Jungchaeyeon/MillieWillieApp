package com.makeusteam.milliewillie.ui.dDay.anniversary

import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityDDayAnniversaryContentBinding
import com.makeusteam.milliewillie.repository.local.RepositoryCached
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