package com.makeus.milliewillie.ui.login

import android.content.Intent
import com.bumptech.glide.util.Util
import com.kakao.sdk.common.util.Utility
import android.os.Build
import androidx.annotation.RequiresApi
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityLoginBinding
import com.makeus.milliewillie.ext.showLongToastSafe
import com.makeus.milliewillie.ui.common.BasicBottomSheetDialogFragment
import com.makeus.milliewillie.ui.common.BasicDialogFragment
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : BaseDataBindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModel<LoginViewModel>()

    private val requestGoogleAuth = 9001

    @RequiresApi(Build.VERSION_CODES.P)
    override fun ActivityLoginBinding.onBind() {
        vi = this@LoginActivity
        vm = viewModel
        viewModel.bindLifecycle(this@LoginActivity)

        val keyHash = Utility.getKeyHash(this@LoginActivity)
        Log.e(keyHash)

        //center dialog
//        BasicDialogFragment.getInstance()
//                .setTitle("예제 타이틀")
//                .setSubTitle("예제 서브 타이틀 (설정 안하면 안보임)")
//                .setContent("내용")
//                .setOnClickOk {
//                    "확인 클릭".showLongToastSafe()
//                }.show(supportFragmentManager)

        //center dialog
//        BasicDialogFragment.getInstance()
//                .setTitle("예제 타이틀")
//                .setSubTitle("예제 서브 타이틀 (설정 안하면 안보임)")
//                .setContent("내용")
//                .setOnClickOk {
//                    "확인 클릭".showLongToastSafe()
//                }.show(supportFragmentManager)
//
//        //BottomSheet dialog
//        BasicBottomSheetDialogFragment.getInstance()
//                .setTitle("예제 타이틀")
//                .setContent("내용")
//                .setOnClickOk {
//                    "확인 클릭".showLongToastSafe()
//                }.show(supportFragmentManager)
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

    fun nextStep(isSuccess : Boolean) {
        if (isSuccess){
            ActivityNavigator.with(this).name().start()
        } else {
            "로그인에 실패했습니다.".showLongToastSafe()
        }
    }
}