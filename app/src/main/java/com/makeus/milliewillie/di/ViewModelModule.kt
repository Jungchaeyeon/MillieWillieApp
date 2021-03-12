package com.makeus.milliewillie.di

import com.makeus.milliewillie.ui.WelcomeViewModel
import com.makeus.milliewillie.ui.LoginViewModel
import com.makeus.milliewillie.ui.map.MapViewModel
import com.makeus.milliewillie.ui.dDay.DdayViewModel
import com.makeus.milliewillie.ui.dDay.birthday.BirthdayViewModel
import com.makeus.milliewillie.ui.dDay.certification.CertificationViewModel
import com.makeus.milliewillie.ui.dDay.ncee.NceeViewModel
import com.makeus.milliewillie.ui.UserViewModel
import com.makeus.milliewillie.ui.routine.ExerciseSetViewModel
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel
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
}