package com.makeus.milliewillie.di

import com.makeus.milliewillie.ui.*
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.ui.intro.WelcomeViewModel
import com.makeus.milliewillie.ui.login.LoginViewModel
import com.makeus.milliewillie.ui.plan.MakePlanViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { WelcomeViewModel() }
    single { UserViewModel() }
    viewModel { MainViewModel() }
    single {  MakePlanViewModel() }
}