package com.makeus.milliewillie.ui

import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroEnlistDate2Binding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_intro_enlist_date2.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_basic.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class IntroEnlistDateActivity2 :
    BaseDataBindingActivity<ActivityIntroEnlistDate2Binding>(R.layout.activity_intro_enlist_date2) {

    private val repositoryCached by inject<RepositoryCached>()
    val viewModel by viewModel<UserViewModel>()

    override fun ActivityIntroEnlistDate2Binding.onBind() {
        vi = this@IntroEnlistDateActivity2
        vm = viewModel
        viewModel.bindLifecycle(this@IntroEnlistDateActivity2)

        viewModel.run {

//            customDialog1.date.observe(
//                this@IntroEnlistDateActivity2,
//                androidx.lifecycle.Observer { liveBtnEnlist.value = customDialog1.date.value })
//            customDialog2.date.observe(
//                this@IntroEnlistDateActivity2,
//                androidx.lifecycle.Observer { liveBtnDischarge.value = customDialog2.date.value })
//            customDialog6.date.observe(
//                this@IntroEnlistDateActivity2,
//                androidx.lifecycle.Observer { liveBtnPromotion.value = customDialog6.date.value })
        }


    }

    fun onClickDate(view: View) {

//        when (view.id) {
//            R.id.btn_enlist -> {
//                viewModel.customDialog1.show(supportFragmentManager, "custom_dialog")
//            }
//            R.id.btn_discharge -> {
//                viewModel.customDialog2.show(supportFragmentManager, "custom_dialog")
//            }
//            R.id.btn_promotion -> {
//                viewModel.customDialog6.show(supportFragmentManager, "custom_dialog")
//            }
//
//        }

    }

    fun onClickDone() {
        if(btn_discharge.text.isEmpty() || btn_promotion.text.isEmpty()){
            "날짜를 선택해주세요.".showShortToastSafe()
        }
        ActivityNavigator.with(this).goal().start()
    }
    fun onClickBack(){
      onBackPressed()
    }

}