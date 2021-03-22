package com.makeus.milliewillie.ui.intro

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroEnlistDateSergeantBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_intro_enlist_date_sergeant.*
import kotlinx.android.synthetic.main.activity_make_plan.*
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


    fun onClickDate(position: Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)
            }.show(supportFragmentManager)
    }

    fun onClickDone() = if (btn_discharge.text.isEmpty() || btn_promotion.text.isEmpty()) {
        Snackbar.make(this.layout_sergeant, "날짜를 선택해주세요", Snackbar.LENGTH_LONG).show();
    } else {
        ActivityNavigator.with(this).goal().start()
        repositoryCached.setValue(LocalKey.ENDDATE, viewModel.liveDateButtonList[1])
        repositoryCached.setValue(LocalKey.ENDDDAY,viewModel.calDday(repositoryCached.getEnd()))
       // repositoryCached.setValue(LocalKey.NEXTDDAY,viewModel.calDday(repositoryCached.getNextDDay()))

    }

    override fun onResume() {
        super.onResume()
        viewModel.liveServiceId.postValue(2)
        viewModel.enlistDataInit()
    }

}