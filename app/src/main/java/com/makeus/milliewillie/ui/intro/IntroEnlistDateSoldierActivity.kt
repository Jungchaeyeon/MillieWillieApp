package com.makeus.milliewillie.ui.intro

import com.google.android.material.snackbar.Snackbar
import  com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroEnlistDateSoldierBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_basic.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class IntroEnlistDateSoldierActivity :
    BaseDataBindingActivity<ActivityIntroEnlistDateSoldierBinding>(R.layout.activity_intro_enlist_date_soldier) {

    private val viewModel by viewModel<UserViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    override fun ActivityIntroEnlistDateSoldierBinding.onBind() {
        vi = this@IntroEnlistDateSoldierActivity
        vm = viewModel
        viewModel.bindLifecycle(this@IntroEnlistDateSoldierActivity)


    }

    fun onClickDate(position: Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)
                if(position == 0) {
                    Log.d(it)
                    viewModel.calculateDay(it)
                }
            }.show(supportFragmentManager)
    }

    fun onClickDone() {
        if (viewModel.enlistValueTest()) {
            Log.e("실행1")
            repositoryCached.setValue(
                LocalKey.ENDDDAY,
                viewModel.calDday(btn_discharge.text.toString()).toString()
            )
            Log.e("실행2")
            ActivityNavigator.with(this).goal().start()
        }
        else{
            Log.e("실행3")
            Snackbar.make(this.layout_enlistSet,"날짜 정보가 옳바르지 않습니다.",Snackbar.LENGTH_LONG).show()

        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.enlistDataInit()
    }
}