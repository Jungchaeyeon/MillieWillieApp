package com.makeusteam.milliewillie.ui.intro

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityIntroSettingNameBinding
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.SampleToast
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class IntroSettingNameActivity :
    BaseDataBindingActivity<ActivityIntroSettingNameBinding>(R.layout.activity_intro_setting_name) {

    val viewModel by viewModel<UserViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    override fun ActivityIntroSettingNameBinding.onBind() {
        vi = this@IntroSettingNameActivity
        openKeyboard()
    }

    fun onClickNext() {
        if (edt_name.text.isNotEmpty()) {
            viewModel.usersRequest.name = edt_name.text.toString()
            ActivityNavigator.with(this).typedetail(viewModel.usersRequest).start()
        } else {
            SampleToast.createToast(this,"이름을 입력해 주세요.")?.show()
        }
    }
    fun onClickBack(){
       onBackPressed()
    }

    private fun openKeyboard() {
        val inputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(edt_name.windowToken, 0)
    }
    fun onClickClear(){
        edt_name.text.clear()
    }



}