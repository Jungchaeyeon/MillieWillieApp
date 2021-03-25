package com.makeus.milliewillie.di

import com.makeus.milliewillie.ui.MainViewModel
import com.makeus.milliewillie.ui.dDay.DdayViewModel
import com.makeus.milliewillie.ui.dDay.birthday.BirthdayViewModel
import com.makeus.milliewillie.ui.dDay.certification.CertificationViewModel
import com.makeus.milliewillie.ui.dDay.ncee.NceeViewModel
import com.makeus.milliewillie.ui.holiday.HoliViewModel
import com.makeus.milliewillie.ui.home.tab3.EmoViewModel
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.ui.intro.WelcomeViewModel
import com.makeus.milliewillie.ui.login.LoginViewModel
import com.makeus.milliewillie.ui.map.MapViewModel
import com.makeus.milliewillie.ui.plan.MakePlanViewModel
import com.makeus.milliewillie.ui.routine.ExerciseSetViewModel
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.get
import org.koin.core.scope.get
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get() , get()) }
    viewModel { DdayViewModel() }
    viewModel { BirthdayViewModel() }
    viewModel { CertificationViewModel() }
    viewModel { NceeViewModel() }
    viewModel { WelcomeViewModel() }
    viewModel { MapViewModel() }
    viewModel { MakeRoutineViewModel() }
    viewModel { ExerciseSetViewModel() }
    viewModel { UserViewModel(get(),get()) }
    viewModel {  MakePlanViewModel(get()) }//추후 수정
    viewModel { MainViewModel() }
    viewModel { EmoViewModel() }
    viewModel { HoliViewModel() }
}