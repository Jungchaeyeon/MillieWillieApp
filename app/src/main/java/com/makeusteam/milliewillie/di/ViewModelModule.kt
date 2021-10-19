package com.makeusteam.milliewillie.di

import com.makeusteam.milliewillie.ui.MainCalendarViewModel
import com.makeusteam.milliewillie.ui.home.tab1.HomeViewModel
import com.makeusteam.milliewillie.ui.MainViewModel
import com.makeusteam.milliewillie.ui.dDay.DdayOutputCNViewModel
import com.makeusteam.milliewillie.ui.dDay.DdayViewModel
import com.makeusteam.milliewillie.ui.dDay.anniversary.DdayOutputAnniversaryViewModel
import com.makeusteam.milliewillie.ui.dDay.birthday.BirthdayViewModel
import com.makeusteam.milliewillie.ui.dDay.certification.CertificationViewModel
import com.makeusteam.milliewillie.ui.dDay.ncee.NceeViewModel
import com.makeusteam.milliewillie.ui.holiday.HoliViewModel
import com.makeusteam.milliewillie.ui.home.tab2.WorkoutViewModel
import com.makeusteam.milliewillie.ui.home.tab3.EmoViewModel
import com.makeusteam.milliewillie.ui.home.tab4.InfoViewModel
import com.makeusteam.milliewillie.ui.info.AccountViewModel
import com.makeusteam.milliewillie.ui.intro.UserViewModel
import com.makeusteam.milliewillie.ui.intro.WelcomeViewModel
import com.makeusteam.milliewillie.ui.login.LoginViewModel
import com.makeusteam.milliewillie.ui.map.MapViewModel
import com.makeusteam.milliewillie.ui.mypage.MyPageEditViewModel
import com.makeusteam.milliewillie.ui.plan.MakePlanViewModel
import com.makeusteam.milliewillie.ui.plan.PlanOutputViewModel
import com.makeusteam.milliewillie.ui.routine.ExerciseSetViewModel
import com.makeusteam.milliewillie.ui.routine.MakeRoutineViewModel
import com.makeusteam.milliewillie.ui.profile.ProfileViewModel
import com.makeusteam.milliewillie.ui.report.ReportViewModel
import com.makeusteam.milliewillie.ui.todayWorkout.TodayWorkoutViewModel
import com.makeusteam.milliewillie.ui.weightRecord.WeightRecordViewModel
import com.makeusteam.milliewillie.ui.workoutStart.WorkoutStartViewModel
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
    viewModel {  MakePlanViewModel(get(),get()) }//추후 수정
    viewModel { MainViewModel(get(),get()) }
    viewModel { HomeViewModel(get(),get()) }
    viewModel { WorkoutViewModel(get(), get()) }
    viewModel { TodayWorkoutViewModel(get()) }
    viewModel { WorkoutStartViewModel(get()) }
    viewModel { InfoViewModel(get()) }
    viewModel { MainCalendarViewModel(get(),get()) }
    viewModel { WeightRecordViewModel(get()) }
    viewModel { EmoViewModel(get(),get()) }
    viewModel { HoliViewModel(get(),get()) }
    viewModel {MyPageEditViewModel(get(),get())}
    viewModel { PlanOutputViewModel(get(),get()) }
    viewModel { ReportViewModel(get()) }
    viewModel { AccountViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { DdayOutputCNViewModel(get()) }
    viewModel { DdayOutputAnniversaryViewModel(get()) }
}