package com.makeus.milliewillie.di

import com.makeus.milliewillie.ui.WelcomeViewModel
import com.makeus.milliewillie.ui.LoginViewModel
import com.makeus.milliewillie.ui.MainViewModel
import com.makeus.milliewillie.ui.UserViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { WelcomeViewModel() }
    viewModel { UserViewModel() }
    viewModel { MainViewModel() }
}