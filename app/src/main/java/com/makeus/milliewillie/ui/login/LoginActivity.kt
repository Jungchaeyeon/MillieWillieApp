package com.makeus.milliewillie.ui.login

import android.content.Intent
import com.kakao.sdk.common.util.Utility
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityLoginBinding
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
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

        Log.e(repositoryCached.getToken().toString(),"토큰유무")

        if (repositoryCached.getToken().isNotEmpty()) {
                //1단계 -> jwt 가지고 있니?
            viewModel.firstCheckJmt() {
                //2단계. 유효한 토큰?
                if (it) {
                    Log.e(it.toString(), "유효한 토큰-> 메인으로")
                    ActivityNavigator.with(this).main().start()
                } else {
                    Log.e(it.toString(), "유효하지 않은 토큰 -> 로그인으로")
                }
            }
        } else {

        }

    }

    companion object {
        var deviceToken: String = ""
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
//            loginType = MyApplication.LOGINTYPE.GOOGLE
            viewModel.getFcmToken {
                deviceToken = it
                Log.e("deviceToken : ${deviceToken}")
            }
            viewModel.onRequestLoginWithGoogle(this, data) {
                Log.e("onRequestLoginWithGoogle")
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
    fun nextStepGoogle(isSuccess: Boolean){
        if(isSuccess){
            Log.e(repositoryCached.getIsMember().toString(),"가입멤버인가")
            if(!repositoryCached.getIsMember()){
                ActivityNavigator.with(this).welcome().start()
            }
            else{
                ActivityNavigator.with(this).main().start()
                Log.e(repositoryCached.getToken(),"jwt")
            }
        }else {
            if(!repositoryCached.getIsMember()){
            }else{
                Snackbar.make(this.mainLayout,"로그인에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }
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
        Log.e("두번거침")
        if (isSuccess) {
            Log.e("onRequestLoginWithGoogle2")
            Log.e(repositoryCached.getIsMember().toString(),"가입멤버인가")
            if(!repositoryCached.getIsMember()){
                ActivityNavigator.with(this).welcome().start()
            }
            else{
                ActivityNavigator.with(this).main().start()
                Log.e(repositoryCached.getToken(),"jwt")
            }
        } else {
            Log.e("onRequestLoginWithGoogle2")
            if(!repositoryCached.getIsMember()){
            }else{
                Snackbar.make(this.mainLayout,"로그인에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

}