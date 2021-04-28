package com.makeusteam.milliewillie.di

import com.makeusteam.milliewillie.BuildConfig
import com.makeusteam.milliewillie.network.adapter.ApiCallAdapterFactory
import com.makeusteam.milliewillie.network.adapter.OkHttpClient
import com.makeusteam.milliewillie.network.api.Api
import com.makeusteam.milliewillie.util.ConnectivityHelper
import com.makeusteam.milliewillie.util.GsonFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = module {
    single {
        OkHttpClient.createOkHttpClient(get())
    }
    single {
        Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .client(get())
                .addConverterFactory(GsonConverterFactory.create(GsonFactory.get()))
                .addCallAdapterFactory(ApiCallAdapterFactory())
                .build()
    }
    single { (get() as Retrofit).create(Api::class.java) }
    single { ConnectivityHelper.get(androidContext()) }

}
