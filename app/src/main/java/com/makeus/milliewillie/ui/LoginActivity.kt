package com.makeus.milliewillie.ui

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityLoginBinding
import com.makeus.milliewillie.ext.showLongToastSafe
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseDataBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModel<LoginViewModel>()

    override fun ActivityLoginBinding.onBind() {
        vi = this@LoginActivity
        vm = viewModel
        viewModel.bindLifecycle(this@LoginActivity)
    }

    fun onClickKakaoLogin(){
        viewModel.onClickKakaoLogin(this) {
            if (it){
                ActivityNavigator.with(this).main().start()
            } else {
                "로그인에 실패했습니다.".showLongToastSafe()
            }
        }
    }
}