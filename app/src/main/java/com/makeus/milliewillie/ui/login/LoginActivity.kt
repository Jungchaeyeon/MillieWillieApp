package com.makeus.milliewillie.ui.login

import android.content.Intent
import com.bumptech.glide.util.Util
import com.kakao.sdk.common.util.Utility
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityLoginBinding
import com.makeus.milliewillie.ext.showLongToastSafe
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.common.BasicBottomSheetDialogFragment
import com.makeus.milliewillie.ui.common.BasicDialogFragment
import com.makeus.milliewillie.util.Log
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseDataBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModel<LoginViewModel>()
    private val requestGoogleAuth = 9001
    private val repositoryCached by inject<RepositoryCached>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositoryCached.setValue(LocalKey.TOKEN,"")
        if (repositoryCached.getToken().isNotEmpty()) {
            Log.e("토큰 없음")
            // /app/user/jwt
            viewModel.firstCheckJmt() {
                if (it==true) {
                    Log.e(it.toString(),"메인으로")
                    ActivityNavigator.with(this).main().start()
                } else {
                    Log.e(it.toString(),"로그인으로")
                    ActivityNavigator.with(this).login().start()
                }
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun ActivityLoginBinding.onBind() {
        vi = this@LoginActivity
        vm = viewModel
        viewModel.bindLifecycle(this@LoginActivity)

        val keyHash = Utility.getKeyHash(this@LoginActivity)
        Log.e(keyHash)


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
        repositoryCached.setValue(LocalKey.SOCIALTYPE, "G")
        startActivityForResult(
            viewModel.getGoogleLoginClient(this)?.signInIntent,
            requestGoogleAuth
        )
    }

    fun onClickKakaoLogin() {
        repositoryCached.setValue(LocalKey.SOCIALTYPE, "K")
        viewModel.getFcmToken {
        }
        viewModel.onClickKakaoLogin(this) {
            nextStep(it)
        }
    }

//    fun nextStep(isSuccess : Boolean) {
//        if (isSuccess){
//            ActivityNavigator.with(this).main().start()
//        } else {
//            "로그인에 실패했습니다.".showLongToastSafe()
//        }
//    }

    fun nextStep(isSuccess: Boolean) {
        if (isSuccess) {
            ActivityNavigator.with(this).main().start()
        } else {
            ActivityNavigator.with(this).name().start()
           // "로그인에 실패했습니다.".showLongToastSafe()
        }
    }
}