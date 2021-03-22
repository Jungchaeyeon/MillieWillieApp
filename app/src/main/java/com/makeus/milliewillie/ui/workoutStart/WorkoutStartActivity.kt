package com.makeus.milliewillie.ui.workoutStart

import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityWorkoutStartBinding
import com.makeus.milliewillie.databinding.ActivityWorkoutStartRecyclerInnerItemBinding
import com.makeus.milliewillie.databinding.ActivityWorkoutStartRecyclerItemBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.model.StartRecyclerItem
import com.makeus.milliewillie.model.TodayRoutines
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel

class WorkoutStartActivity: BaseDataBindingActivity<ActivityWorkoutStartBinding>(R.layout.activity_workout_start) {

    private val viewModel by viewModel<WorkoutStartViewModel>()

    override fun ActivityWorkoutStartBinding.onBind() {
        vi = this@WorkoutStartActivity
        vm = viewModel

        binding.startRecycler.run {
            // 아이템 클릭 리스너
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    val position = child?.let { rv.getChildAdapterPosition(it) }
                    Log.e("$position")
                    if (e.action == MotionEvent.ACTION_UP) {
                        if (position != null) {
                            Log.e("position=== $position")
                            viewModel.onClickItemList(position)
                            return true
                        }
                    }
                    return false
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {

                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

                }

            })
            adapter = BaseDataBindingRecyclerViewAdapter<StartRecyclerItem>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<StartRecyclerItem, ActivityWorkoutStartRecyclerItemBinding>(R.layout.activity_workout_start_recycler_item){
                        vi = this@WorkoutStartActivity
                        item = it
                        startItemRecycler.run {
                            adapter = BaseDataBindingRecyclerViewAdapter<StartRecyclerCircleItem>()
                                .addViewType(
                                    BaseDataBindingRecyclerViewAdapter.MultiViewType<StartRecyclerCircleItem, ActivityWorkoutStartRecyclerInnerItemBinding>(R.layout.activity_workout_start_recycler_inner_item) { innerIt ->
                                        vi = this@WorkoutStartActivity
                                        item = innerIt
                                    })
                        }

                    }
                )

        }


    }


    override fun onResume() {
        super.onResume()
//        viewModel.defaultItemList()
//        viewModel.defaultCircleItemList()
    }

}