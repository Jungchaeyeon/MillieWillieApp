package com.makeus.milliewillie.ui.intro

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroSettingNameBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class IntroSettingNameActivity :
    BaseDataBindingActivity<ActivityIntroSettingNameBinding>(R.layout.activity_intro_setting_name) {

    private val repositoryCached by inject<RepositoryCached>()
    val viewModel by viewModel<UserViewModel>()


    override fun ActivityIntroSettingNameBinding.onBind() {
        vi = this@IntroSettingNameActivity
        openKeyboard()
    }

    fun onClickNext() {
        if (edt_name.text.isNotEmpty()) {
            viewModel.liveUserName.postValue(edt_name.text.toString())
            Log.e("이름",viewModel.liveUserName.value.toString())
            ActivityNavigator.with(this).type().start()
        } else {
            "이름을 입력해 주세요.".showShortToastSafe()
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