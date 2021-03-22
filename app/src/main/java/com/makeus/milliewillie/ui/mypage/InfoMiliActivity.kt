package com.makeus.milliewillie.ui.mypage

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoMiliBinding
import com.makeus.milliewillie.ui.plan.MakePlanViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class InfoMiliActivity :
    BaseDataBindingActivity<ActivityInfoMiliBinding>(R.layout.activity_info_mili) {

    val viewModel by viewModel<MakePlanViewModel>()

    companion object {
        fun getInstance() = InfoMiliActivity()
    }

    override fun ActivityInfoMiliBinding.onBind() {
        vi = this@InfoMiliActivity
        vm = viewModel
        btnX.setPadding(10,10,10,10)
    }
    fun onClickEdit(){
        ActivityNavigator.with(this).infoenlist().start()
    }


}