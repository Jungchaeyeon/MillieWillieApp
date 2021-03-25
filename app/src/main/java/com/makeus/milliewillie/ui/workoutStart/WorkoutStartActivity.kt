package com.makeus.milliewillie.ui.workoutStart

import android.os.Build
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityWorkoutStartBinding
import com.makeus.milliewillie.databinding.ActivityWorkoutStartRecyclerInnerItemBinding
import com.makeus.milliewillie.databinding.ActivityWorkoutStartRecyclerItemBinding
import com.makeus.milliewillie.model.StartRecyclerCircleItem
import com.makeus.milliewillie.model.StartRecyclerItem
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class WorkoutStartActivity: BaseDataBindingActivity<ActivityWorkoutStartBinding>(R.layout.activity_workout_start) {

    private val viewModel by viewModel<WorkoutStartViewModel>()

    private var position by Delegates.notNull<Int>()

    override fun ActivityWorkoutStartBinding.onBind() {
        vi = this@WorkoutStartActivity
        vm = viewModel

        binding.startRecycler.run {
            // 아이템 클릭 리스너
            addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    val child = rv.findChildViewUnder(e.x, e.y)
                    val position = child?.let { rv.getChildAdapterPosition(it) }
                    if (e.action == MotionEvent.ACTION_MOVE) return false
                    else if (e.action == MotionEvent.ACTION_UP) {
                        Log.e("$position")
                        if (position != null) {
                            this@WorkoutStartActivity.position = position
                            return false
                        }
                        return true
                    }
                    return false
                }
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
            adapter = BaseDataBindingRecyclerViewAdapter<StartRecyclerItem>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<StartRecyclerItem, ActivityWorkoutStartRecyclerItemBinding>(R.layout.activity_workout_start_recycler_item){
                        vi = this@WorkoutStartActivity
                        item = it
                        vm = viewModel
                        this.startItemRecycler.run {
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

    fun stopWatch() {
//        timer(period = 1000, initialDelay = 1000) {
//            viewModel.increaseSec(viewModel.liveDataTimeSec.value!!)
//            binding.startTextSec.text = viewModel.liveDataTimeSec.toString()
//        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.defaultItemList()
        viewModel.defaultCircleItemList()
    }

}