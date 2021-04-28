package com.makeusteam.milliewillie.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.kakao.sdk.common.util.Utility
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.MyApplication
import com.makeusteam.milliewillie.MyApplication.Companion.loginType
import com.makeusteam.milliewillie.MyApplication.Companion.isLogout
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityLoginBinding
import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseDataBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModel<LoginViewModel>()
    private val requestGoogleAuth = 9001
    private val repositoryCached by inject<RepositoryCached>()
    private val apiRepository by  inject<ApiRepository>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e(repositoryCached.getToken().toString(),"토큰유무")

        Log.e("isLogout = $isLogout")
        Log.e("repositoryCached.getInApp() = ${repositoryCached.getInApp()}")
        Log.e(repositoryCached.getIsMember().toString(),"GETISMEMBER")
        Log.e(repositoryCached.getToken().toString(),"GETTOKEN")


        if(!isLogout) {
            //-> true, logout상태면 true
                //isLogout == false -> 로그인 상태
            if(repositoryCached.getInApp() =="KF") {
                if (repositoryCached.getToken().isNotEmpty()) {
                    //1단계 -> jwt 가지고 있니?
                    viewModel.firstCheckJmt() {
                        //2단계. 유효한 토큰?
                        if (it) {
                            apiRepository.kakaoLogin()
                            loginType = MyApplication.LOGINTYPE.KAKAO
                            repositoryCached.setValue(LocalKey.LOGINTYPE, loginType)
                            Log.e(it.toString(), "유효한 토큰-> 메인으로")
                            if(repositoryCached.getIsMember()){
                                ActivityNavigator.with(this).main().start()}
                            else{
                                ActivityNavigator.with(this).welcome().start()
                            }
                        } else {
                            Log.e(it.toString(), "유효하지 않은 토큰 -> 로그인으로")
                        }
                    }
                } else {
                    repositoryCached.setValue(LocalKey.TOKEN,"")
                }
            } else if (repositoryCached.getInApp() =="GF") {
                if (repositoryCached.getToken().isNotEmpty()) {
                    //1단계 -> jwt 가지고 있니?
                    viewModel.firstCheckJmt() {
                        //2단계. 유효한 토큰?
                        if (it) {
                            apiRepository.googleLogin()
                            loginType = MyApplication.LOGINTYPE.GOOGLE
                            repositoryCached.setValue(LocalKey.LOGINTYPE, loginType)
                            Log.e(it.toString(), "유효한 토큰-> 메인으로")
                            if(repositoryCached.getIsMember()){
                                ActivityNavigator.with(this).main().start()}
                            else{
                                ActivityNavigator.with(this).welcome().start()
                            }
                        } else {
                            Log.e(it.toString(), "유효하지 않은 토큰 -> 로그인으로")
                        }
                    }
                } else {

                }
            }
        }else{
            Log.e(repositoryCached.getIsMember().toString(),"isMember?")
        //로그아웃 상태 -> isMember이고 회원으로 이동
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
            loginType = MyApplication.LOGINTYPE.GOOGLE
            repositoryCached.setValue(LocalKey.LOGINTYPE, loginType)
            repositoryCached.setValue(LocalKey.SOCIALTYPE, "G")
            viewModel.getFcmToken {
                deviceToken = it
            }
            viewModel.onRequestLoginWithGoogle(this, data) {
                repositoryCached.setValue(LocalKey.INAPP, "GF")
                repositoryCached.setValue(LocalKey.SOCIALTYPE,"G")
                Log.e("onRequestLoginWithGoogle")
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
//    fun nextStepGoogle(isSuccess: Boolean){
//        if(isSuccess){
//            Log.e(repositoryCached.getIsMember().toString(),"가입멤버인가")
//            if(!repositoryCached.getIsMember()){
//                ActivityNavigator.with(this).welcome().start()
//            }
//            else{
//                ActivityNavigator.with(this).main().start()
//                Log.e(repositoryCached.getToken(),"jwt")
//            }
//        }else {
//            if(!repositoryCached.getIsMember()){
//            }else{
//                Snackbar.make(this.mainLayout,"로그인에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//    }


    fun onClickKakaoLogin() {
        repositoryCached.setValue(LocalKey.SOCIALTYPE, "K")
        viewModel.getFcmToken {
        }
        viewModel.onClickKakaoLogin(this) {
            loginType = MyApplication.LOGINTYPE.KAKAO
            repositoryCached.setValue(LocalKey.LOGINTYPE, loginType)
            repositoryCached.setValue(LocalKey.INAPP, "KF")
            nextStep(it)
        }

    }

    fun nextStep(isSuccess: Boolean) {
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