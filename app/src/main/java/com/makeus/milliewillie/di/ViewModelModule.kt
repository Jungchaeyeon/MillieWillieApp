package com.makeus.milliewillie.di

import com.makeus.milliewillie.ui.*
import com.makeus.milliewillie.ui.intro.WelcomeViewModel
import com.makeus.milliewillie.ui.login.LoginViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { WelcomeViewModel() }
    viewModel { UserViewModel() }
    viewModel { MainViewModel() }
    viewModel { MakePlanViewModel() }
}