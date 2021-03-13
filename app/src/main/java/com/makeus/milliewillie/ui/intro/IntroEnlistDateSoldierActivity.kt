package com.makeus.milliewillie.ui.intro

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroEnlistDateSoldierBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_basic.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class IntroEnlistDateSoldierActivity :
    BaseDataBindingActivity<ActivityIntroEnlistDateSoldierBinding>(R.layout.activity_intro_enlist_date_soldier) {

    private val viewModel by viewModel<UserViewModel>()

    override fun ActivityIntroEnlistDateSoldierBinding.onBind() {
        vi = this@IntroEnlistDateSoldierActivity
        vm = viewModel
        viewModel.bindLifecycle(this@IntroEnlistDateSoldierActivity)

    }

    fun onClickDate(position : Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)
            }.show(supportFragmentManager)
    }

    fun onClickDone() {
        if(btn_discharge.text.isEmpty()){
            "날짜를 선택해주세요.".showShortToastSafe()
        }
        ActivityNavigator.with(this).goal().start()
    }


}