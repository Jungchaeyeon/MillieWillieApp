package com.makeusteam.milliewillie.ui.login

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
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
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

                                repositoryCached.setValue(LocalKey.TOKEN, googleSignInAccount.idToken)

                                requestGoogleLogin()
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

    fun requestGoogleLogin()=
        apiRepository.googleLogin().subscribe({
            Log.e("requestGoogleLogin true??? ?????????")
        }, {
            it.printStackTrace()
            Log.e("requestGoogle false??? ?????????")
            Log.e(repositoryCached.getToken(),"??????")
        }).disposeOnDestroy(this)


    fun onClickKakaoLogin(context: Context, response: (Boolean) -> Unit) {
        // ??????????????? ???????????? ????????? ?????????????????? ?????????, ????????? ????????????????????? ?????????
        Single.just(LoginClient.instance.isKakaoTalkLoginAvailable(context))
            .flatMap { available ->
                if (available) LoginClient.rx.loginWithKakaoTalk(context)
                else LoginClient.rx.loginWithKakaoAccount(context)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ token ->

                Log.e("?????? ????????? ??? ????????? ?????? ${token.accessToken}")
                repositoryCached.setValue(LocalKey.TOKEN, token.accessToken)

                //?????? ??????
                requestKakaoLogin(response)

            }, { error ->
                error.printStackTrace()
                response.invoke(false)
            }).disposeOnDestroy(this)

    }

    private fun requestKakaoLogin(response: (Boolean) -> Unit) {
        apiRepository.kakaoLogin().subscribe({
            Log.e("requestKakaoLogin true")
            UserApiClient.rx.me()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { user ->
//                    if(user.kakaoAccount?.profile?.thumbnailImageUrl.isNullOrEmpty()){
//                        MyApplication.userProfileImgUrl= "https://ibb.co/ctvZwnT"
//                        Log.e(MyApplication.userProfileImgUrl.toString(),"url ?????????")
//                    }
                    Log.e(
                        "????????? ?????? ?????? ??????" +
                                "\n????????????: ${user.id}" +
                                "\n?????????: ${user.kakaoAccount?.email}" +
                                "\n?????????: ${user.kakaoAccount?.profile?.nickname}" +
                                "\n???????????????: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                    )
                    response.invoke(true)
                }
        }, {
            Log.e("requestKakaoLogin false")
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
                    "????????? ?????? ?????? ??????" +
                            "\n????????????: ${user.id}" +
                            "\n?????????: ${user.kakaoAccount?.email}" +
                            "\n?????????: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n???????????????: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
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

    fun firstCheckJmt(response: (Boolean) -> Unit) {
        if (repositoryCached.getToken().isNotEmpty()) {
            apiRepository.jwt().subscribe({
                Log.e("jwt????????? ???????????? ??????")
                response.invoke(true)
            }, {
                it.printStackTrace()
                Log.e("jwt???????????? ?????? ???????????? ??????")
                response.invoke(false)
            }).disposeOnDestroy(this)
        }
    }
}