package com.makeus.milliewillie.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.rx
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.rx
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.R
import com.makeus.milliewillie.di.repositoryModule
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginViewModel(
    val apiRepository: ApiRepository,
    val repositoryCached: RepositoryCached
) : BaseViewModel() {

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var googleSignInClient: GoogleSignInClient? = null

    fun getGoogleLoginClient(activity: Activity): GoogleSignInClient? {
        if (googleSignInClient == null) {
            googleSignInClient = GoogleSignIn.getClient(
                activity, GoogleSignInOptions.Builder(
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                )
                    .requestIdToken(activity.getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            )
        }
        return googleSignInClient
    }

    fun onRequestLoginWithGoogle(
        activity: Activity,
        data: Intent?,
        response: (success: Boolean) -> Unit
    ) {
        GoogleSignIn.getSignedInAccountFromIntent(data).run {
            try {
                getResult(ApiException::class.java)?.let { googleSignInAccount ->
                    Log.e(googleSignInAccount.idToken)
                    auth.signInWithCredential(
                        GoogleAuthProvider.getCredential(
                            googleSignInAccount.idToken,
                            null
                        )
                    ).addOnCompleteListener(activity) { task ->
                        if (task.isSuccessful) {
                            if (auth.currentUser == null || googleSignInAccount.id == null) {
                                response.invoke(false)
                                return@addOnCompleteListener
                            }

                            auth.currentUser?.let {
                                Log.e(googleSignInAccount.id)
                                Log.e(googleSignInAccount.idToken, "AccessToken")
                                Log.e(googleSignInAccount.isExpired.toString(), "AccessToken")

                                response.invoke(true)
                            }
                        } else {
                            response.invoke(false)
                        }
                    }
                }
            } catch (e: ApiException) {
                response.invoke(false)
                e.printStackTrace()
            }
        }
    }

    fun onClickKakaoLogin(context: Context, response: (Boolean) -> Unit) {
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        Single.just(LoginClient.instance.isKakaoTalkLoginAvailable(context))
            .flatMap { available ->
                if (available) LoginClient.rx.loginWithKakaoTalk(context)
                else LoginClient.rx.loginWithKakaoAccount(context)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->
                Log.e("로그인 성공 ${token.accessToken}")
                repositoryCached.setValue(LocalKey.TOKEN, token.accessToken)
                requestKakaoLogin(response)
                //response.invoke(true)
            }, { error ->
                error.printStackTrace()
                response.invoke(false)
            }).disposeOnDestroy(this)
    }

    private fun requestKakaoLogin(response: (Boolean) -> Unit) {
        apiRepository.kakaoLogin().subscribe ({
            response.invoke(true)
        } , {
            it.printStackTrace()
            response.invoke(false)
        }).disposeOnDestroy(this)
    }

    private fun requestKakaoUserInfo(response: (Boolean) -> Unit) {
        UserApiClient.rx.me()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ user ->
                Log.e(
                    "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )

                response.invoke(true)
            }, { error ->
                error.printStackTrace()
                response.invoke(false)
            }).disposeOnDestroy(this)
    }

    fun getFcmToken(response: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            response(it.result ?: "")
        }
    }
}