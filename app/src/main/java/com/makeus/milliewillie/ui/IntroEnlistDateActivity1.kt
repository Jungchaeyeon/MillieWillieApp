package com.makeus.milliewillie.ui

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroEnlistDate1Binding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.ui.fragment.DatePickerBasicBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_intro_enlist_date2.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_basic.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class IntroEnlistDateActivity1 :
    BaseDataBindingActivity<ActivityIntroEnlistDate1Binding>(R.layout.activity_intro_enlist_date1) {

    private val viewModel by viewModel<UserViewModel>()

    override fun ActivityIntroEnlistDate1Binding.onBind() {
        vi = this@IntroEnlistDateActivity1
        vm = viewModel
        viewModel.bindLifecycle(this@IntroEnlistDateActivity1)

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

    fun onClickBack(){
        onBackPressed()
    }

}