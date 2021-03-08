package com.makeus.milliewillie.ui

import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroEnlistDate1Binding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.UserViewModel
import kotlinx.android.synthetic.main.activity_intro_enlist_date2.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_basic.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class IntroEnlistDateActivity1 :
    BaseDataBindingActivity<ActivityIntroEnlistDate1Binding>(R.layout.activity_intro_enlist_date1) {

    private val repositoryCached by inject<RepositoryCached>()
    val viewModel by viewModel<UserViewModel>()

    override fun ActivityIntroEnlistDate1Binding.onBind() {
        vi = this@IntroEnlistDateActivity1
        vm = viewModel
        viewModel.bindLifecycle(this@IntroEnlistDateActivity1)

        viewModel.run{
            liveBtnDischarge.postValue(today())
            customDialog1.date.observe(this@IntroEnlistDateActivity1, androidx.lifecycle.Observer { liveBtnEnlist.value = customDialog1.date.value})
            customDialog2.date.observe(this@IntroEnlistDateActivity1, androidx.lifecycle.Observer { liveBtnDischarge.value = customDialog2.date.value})
            customDialog3.date.observe(this@IntroEnlistDateActivity1, androidx.lifecycle.Observer { liveBtnPrivate.value = customDialog3.date.value})
            customDialog4.date.observe(this@IntroEnlistDateActivity1, androidx.lifecycle.Observer { liveBtnCorporal.value = customDialog4.date.value})
            customDialog5.date.observe(this@IntroEnlistDateActivity1, androidx.lifecycle.Observer { liveBtnSergeant.value = customDialog5.date.value})

        }

    }

    fun onClickDate(view: View) {

        when (view.id) {
            R.id.btn_enlist -> {
                viewModel.customDialog1.show(supportFragmentManager, "custom_dialog")
            }
            R.id.btn_discharge -> {
                viewModel.customDialog2.show(supportFragmentManager, "custom_dialog")
            }
            R.id.btn_private_prom -> {
                viewModel.customDialog3.show(supportFragmentManager, "custom_dialog")
            }
            R.id.btn_corporal_prom -> {
                viewModel.customDialog4.show(supportFragmentManager, "custom_dialog")
            }
            R.id.btn_sergeant_prom -> {
                viewModel.customDialog5.show(supportFragmentManager, "custom_dialog")
            }

        }

    }
    fun onClickDone() {
        if(btn_discharge.text.isEmpty()==true){
            "날짜를 선택해주세요.".showShortToastSafe()
        }
        ActivityNavigator.with(this).goal().start()
    }
    fun onClickBack(){
        onBackPressed()
    }

}