package com.makeus.milliewillie.di

import com.makeus.milliewillie.ui.map.MapViewModel
import com.makeus.milliewillie.ui.dDay.DdayViewModel
import com.makeus.milliewillie.ui.dDay.birthday.BirthdayViewModel
import com.makeus.milliewillie.ui.dDay.certification.CertificationViewModel
import com.makeus.milliewillie.ui.dDay.ncee.NceeViewModel
import com.makeus.milliewillie.ui.routine.ExerciseSetViewModel
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel
import com.makeus.milliewillie.ui.*
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.ui.intro.WelcomeViewModel
import com.makeus.milliewillie.ui.login.LoginViewModel
import com.makeus.milliewillie.ui.plan.MakePlanViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { DdayViewModel() }
    viewModel { BirthdayViewModel() }
    viewModel { CertificationViewModel() }
    viewModel { NceeViewModel() }
    viewModel { WelcomeViewModel() }
    viewModel { UserViewModel() }
    viewModel { MapViewModel() }
    viewModel { MakeRoutineViewModel() }
    viewModel { ExerciseSetViewModel() }
    viewModel { MainViewModel() }
    single {  MakePlanViewModel() }
}