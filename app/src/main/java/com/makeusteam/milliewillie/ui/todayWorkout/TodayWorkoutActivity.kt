package com.makeusteam.milliewillie.ui.todayWorkout

import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityTodayWorkoutBinding

class TodayWorkoutActivity: BaseDataBindingActivity<ActivityTodayWorkoutBinding>(R.layout.activity_today_workout) {



    override fun ActivityTodayWorkoutBinding.onBind() {
        vi = this@TodayWorkoutActivity

        onClickViewChange(1)

    }

    fun onClickViewChange(distinction: Int) {
        val fmbt = supportFragmentManager.beginTransaction()
        when (distinction) {
            1 -> { // 캘린더
                fmbt.replace(R.id.today_workout_frame, TodayWorkoutCalendarFragment()).commitAllowingStateLoss()
            }
            2 -> { // 피드
                fmbt.replace(R.id.today_workout_frame, TodayWorkoutFeedFragment()).commitAllowingStateLoss()
            }
        }

    }



}