package com.makeus.milliewillie.di

import com.makeus.milliewillie.ui.MainViewModel
import com.makeus.milliewillie.ui.dDay.DdayViewModel
import com.makeus.milliewillie.ui.dDay.birthday.BirthdayViewModel
import com.makeus.milliewillie.ui.dDay.certification.CertificationViewModel
import com.makeus.milliewillie.ui.dDay.ncee.NceeViewModel
import com.makeus.milliewillie.ui.routine.ExerciseSetViewModel
import com.makeus.milliewillie.ui.routine.MakeRoutineViewModel
import com.makeus.milliewillie.ui.*
import com.makeus.milliewillie.ui.home.tab2.WorkoutViewModel
import com.makeus.milliewillie.ui.home.tab4.InfoViewModel
import com.makeus.milliewillie.ui.holiday.HoliViewModel
import com.makeus.milliewillie.ui.home.tab3.EmoViewModel
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.ui.intro.WelcomeViewModel
import com.makeus.milliewillie.ui.login.LoginViewModel
import com.makeus.milliewillie.ui.map.MapViewModel
import com.makeus.milliewillie.ui.plan.MakePlanViewModel
import com.makeus.milliewillie.ui.report.ReportViewModel
import com.makeus.milliewillie.ui.todayWorkout.TodayWorkoutViewModel
import com.makeus.milliewillie.ui.weightRecord.WeightRecordViewModel
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { LoginViewModel(get() , get()) }
    viewModel { DdayViewModel(get(), get()) }
    viewModel { BirthdayViewModel() }
    viewModel { CertificationViewModel() }
    viewModel { NceeViewModel() }
    viewModel { WelcomeViewModel() }
    viewModel { MapViewModel() }
    viewModel { MakeRoutineViewModel(get()) }
    viewModel { ExerciseSetViewModel() }
    viewModel { UserViewModel(get(),get()) }
    single {  MakePlanViewModel() }//추후 수정
    viewModel { MainViewModel() }
    viewModel { WorkoutViewModel(get()) }
    viewModel { TodayWorkoutViewModel(get()) }
    viewModel { WorkoutStartViewModel(get()) }
    viewModel { InfoViewModel() }
    viewModel { WeightRecordViewModel(get()) }
    viewModel { EmoViewModel() }
    viewModel { HoliViewModel() }
    viewModel { ReportViewModel(get()) }
}