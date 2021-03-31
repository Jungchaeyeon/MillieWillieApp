package com.makeus.milliewillie.ui.dDay

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityDDayCertiNceeContentBinding
import com.makeus.milliewillie.repository.local.RepositoryCached
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class DdayOutputCNActivity: BaseDataBindingActivity<ActivityDDayCertiNceeContentBinding>(R.layout.activity_d_day_certi_ncee_content) {

    private val viewModel by viewModel<DdayOutputCNViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    override fun ActivityDDayCertiNceeContentBinding.onBind() {
        vi = this@DdayOutputCNActivity
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