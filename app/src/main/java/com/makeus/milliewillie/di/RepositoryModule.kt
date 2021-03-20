package com.makeus.milliewillie.di

import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.repository.local.RepositoryDevicePreference
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single<RepositoryCached> { RepositoryDevicePreference(androidContext()) }   //not used
    single { ApiRepository(get(), get()) }
}