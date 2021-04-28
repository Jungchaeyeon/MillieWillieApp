package com.makeusteam.milliewillie.di

import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.repository.local.RepositoryDevicePreference
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<RepositoryCached> { RepositoryDevicePreference(androidContext()) }   //not used
    single { ApiRepository(get(), get()) }
}