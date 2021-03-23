package com.makeus.milliewillie.ui.login

import android.content.Intent
import com.bumptech.glide.util.Util
import com.kakao.sdk.common.util.Utility
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
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
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseDataBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModel<LoginViewModel>()
    private val requestGoogleAuth = 9001
    private val repositoryCached by inject<RepositoryCached>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (repositoryCached.getToken().isNotEmpty()) {
                //1단계 -> jwt 가지고 있니?
            viewModel.firstCheckJmt() {
                //2단계. 유효한 토큰?
                if (it) {
                    Log.e(it.toString(), "메인으로")
                    ActivityNavigator.with(this).main().start()
                } else {
                    Log.e(it.toString(), "로그인으로")
                  //  ActivityNavigator.with(this).login().start()
                }
            }
        } else {

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
            if(!repositoryCached.getIsMember()){
                ActivityNavigator.with(this).welcome().start()
            }else{
                Snackbar.make(this.mainLayout,"로그인에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}