package com.makeus.milliewillie.ui

import android.content.Context
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel : BaseViewModel() {

    fun onClickKakaoLogin(context : Context, response : (Boolean) -> Unit){
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        Single.just(LoginClient.instance.isKakaoTalkLoginAvailable(context))
            .flatMap { available ->
                if (available) LoginClient.rx.loginWithKakaoTalk(context)
                else LoginClient.rx.loginWithKakaoAccount(context)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->
                Log.e("로그인 성공 ${token.accessToken}")
                requestKakaoUserInfo(response)
            }, { error ->
                error.printStackTrace()
                response.invoke(false)
            }).disposeOnDestroy(this)
    }

    private fun requestKakaoUserInfo(response : (Boolean) -> Unit){
        UserApiClient.rx.me()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ user ->
                Log.e("사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")

                response.invoke(true)
            }, { error ->
                error.printStackTrace()
                response.invoke(false)
            }).disposeOnDestroy(this)
    }
}