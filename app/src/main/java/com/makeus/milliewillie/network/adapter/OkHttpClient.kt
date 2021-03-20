package com.makeus.milliewillie.network.adapter


import com.facebook.stetho.okhttp3.StethoInterceptor
import com.makeus.milliewillie.BuildConfig
import com.makeus.milliewillie.repository.local.RepositoryCached
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

object OkHttpClient {

    private const val CONNECT_TIMEOUT = 15L
    private const val WRITE_TIMEOUT = 15L
    private const val READ_TIMEOUT = 15L

    fun createOkHttpClient(repositoryCached: RepositoryCached): OkHttpClient =
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addNetworkInterceptor(StethoInterceptor())
            }
        }.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS) //연결 타임아웃 시간 설정
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS) //쓰기 타임아웃 시간 설정
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS) //읽기 타임아웃 시간 설정
            .addInterceptor(HttpLoggingInterceptor().apply {
                level =
                    if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }).addInterceptor(AuthInterceptor(repositoryCached))
            .build()
}