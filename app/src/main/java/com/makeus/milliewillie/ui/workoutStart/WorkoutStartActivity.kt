package com.makeus.milliewillie.ui.workoutStart

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityWorkoutStartBinding
import org.koin.android.viewmodel.ext.android.viewModel

class WorkoutStartActivity: BaseDataBindingActivity<ActivityWorkoutStartBinding>(R.layout.activity_workout_start) {

    private val viewModel by viewModel<WorkoutStartViewModel>()

    override fun ActivityWorkoutStartBinding.onBind() {
        vi = this@WorkoutStartActivity
        vm = viewModel


    }



}