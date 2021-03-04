package com.makeus.milliewillie.ui

import android.content.Intent
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityLoginBinding
import com.makeus.milliewillie.ext.showLongToastSafe
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseDataBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModel<LoginViewModel>()

    private val requestGoogleAuth = 9001

    override fun ActivityLoginBinding.onBind() {
        vi = this@LoginActivity
        vm = viewModel
        viewModel.bindLifecycle(this@LoginActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestGoogleAuth) {
            viewModel.onRequestLoginWithGoogle(this, data) {
                nextStep(it)
            }
        }
    }

    fun onClickGoogleLogin() {
        startActivityForResult(
            viewModel.getGoogleLoginClient(this)?.signInIntent,
            requestGoogleAuth
        )
    }

    fun onClickKakaoLogin(){
        viewModel.onClickKakaoLogin(this) {
            nextStep(it)
        }
    }

    fun nextStep(isSuccess : Boolean) {
        if (isSuccess){
            ActivityNavigator.with(this).main().start()
        } else {
            "로그인에 실패했습니다.".showLongToastSafe()
        }
    }
}