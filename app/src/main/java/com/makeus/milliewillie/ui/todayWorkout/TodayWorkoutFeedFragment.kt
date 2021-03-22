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

    var liveDataItemImg = R.drawable.icon_arrow_gotodeep

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


    }

    fun onClickEdit() {
        isEdit = !isEdit
        Log.e(isEdit.toString())
        when (isEdit) {
            true -> {
//                binding.todayFeedTextEdit.setText(R.string.complete)
                viewModel.liveDataTextEdit.postValue(R.string.complete)
                liveDataItemImg = R.drawable.icon_delete_btype
                viewModel.defaultRoutineItemList()
            }
            false -> {
//                binding.todayFeedTextEdit.setText(R.string.edit)
                viewModel.liveDataTextEdit.postValue(R.string.edit)
                liveDataItemImg = R.drawable.icon_arrow_gotodeep
                viewModel.defaultRoutineItemList()
            }
        }
    }


}