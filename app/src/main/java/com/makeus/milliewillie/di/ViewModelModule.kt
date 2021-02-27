package com.makeus.milliewillie.di

import com.makeus.milliewillie.ui.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
}