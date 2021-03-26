package com.makeus.milliewillie

import android.app.Application
import android.content.SharedPreferences
import com.facebook.stetho.Stetho
import com.kakao.sdk.common.KakaoSdk
import com.makeus.milliewillie.di.networkModule
import com.makeus.milliewillie.di.repositoryModule
import com.makeus.milliewillie.di.viewModelModule
import com.makeus.milliewillie.network.adapter.RxErrorHandler
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.plugins.RxJavaPlugins
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

class MyApplication : Application() {

    companion object {
        private var instance: MyApplication? = null
        val globalApplicationContext: MyApplication
            get() {
                checkNotNull(instance) { "this application does not inherit CoinoneApplication" }
                return instance as MyApplication
            }

        fun getString(stringId: Int): String = globalApplicationContext.getString(stringId)

        fun getStringArg(stringId: Int, arg: String) {
            String.format(globalApplicationContext.getString(stringId, arg))
        }

//        lateinit var loginType: LOGINTYPE
        var isEnabledPush: Boolean = true

        var isFirstExListSet by Delegates.notNull<Boolean>()

        lateinit var sSharedPreferences: SharedPreferences
        const val MILLI_WILLI = "MILLI_WILLI"
    }

    enum class LOGINTYPE {
        KAKAO,
        GOOGLE
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        startKoin {
            androidContext(this@MyApplication)
            modules(
                listOf(
                    networkModule, repositoryModule, viewModelModule
                )
            )
        }

        RxJavaPlugins.setErrorHandler { RxErrorHandler().processErrorHandler(it) }
        RxJavaPlugins.lockdown()

        // Kakao SDK 초기화
        KakaoSdk.init(this, BuildConfig.KAKAO_APP_KEY)

        sSharedPreferences = applicationContext.getSharedPreferences(MILLI_WILLI, MODE_PRIVATE)
        isFirstExListSet = SharedPreference.getSettingBooleanItem("firstSet")

    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }
}