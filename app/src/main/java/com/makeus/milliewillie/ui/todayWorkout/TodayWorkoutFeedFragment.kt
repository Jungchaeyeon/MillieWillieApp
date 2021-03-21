package com.makeus.milliewillie.ui.todayWorkout

import android.view.View
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentTodayWorkoutFeedBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.model.TodayRoutines
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel

class TodayWorkoutFeedFragment: BaseDataBindingFragment<FragmentTodayWorkoutFeedBinding>(R.layout.fragment_today_workout_feed) {

    private val viewModel by viewModel<TodayWorkoutViewModel>()

    private var isEdit = false

    lateinit var itemView: View

    override fun FragmentTodayWorkoutFeedBinding.onBind() {
        vi = this@TodayWorkoutFeedFragment
        vm = viewModel

        binding.todayFeedImgGotoBack.setOnClickListener {
            (activity as TodayWorkoutActivity).onClickViewChange(1)
        }

        binding.todayFeedRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<TodayRoutines>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<TodayRoutines, WorkoutRoutineRecyclerItemBinding>(R.layout.workout_routine_recycler_item){
                        viFeed = this@TodayWorkoutFeedFragment
                        item = it
                    }
                )
        }

        logTest()

    }

    fun logTest() {
        Log.e("$itemView")
    }


}