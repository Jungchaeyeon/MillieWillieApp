package com.makeus.milliewillie.ui.intro

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroEnlistDateSergeantBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_intro_enlist_date_sergeant.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_basic.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class IntroEnlistDateSergeantActivity :
    BaseDataBindingActivity<ActivityIntroEnlistDateSergeantBinding>(R.layout.activity_intro_enlist_date_sergeant) {

    private val repositoryCached by inject<RepositoryCached>()
    val viewModel by viewModel<UserViewModel>()

    override fun ActivityIntroEnlistDateSergeantBinding.onBind() {
        vi = this@IntroEnlistDateSergeantActivity
        vm = viewModel
        viewModel.bindLifecycle(this@IntroEnlistDateSergeantActivity)
    }

    fun onClickDate(position : Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)
            }.show(supportFragmentManager)
    }

    fun onClickDone() {
        if(btn_discharge.text.isEmpty() || btn_promotion.text.isEmpty()){
            "날짜를 선택해주세요.".showShortToastSafe()
        }
        ActivityNavigator.with(this).goal().start()
    }

    override fun onResume() {
        super.onResume()
        viewModel.liveDateButtonList[1].value=""
        viewModel.liveDateButtonList[2].value=""
    }

}