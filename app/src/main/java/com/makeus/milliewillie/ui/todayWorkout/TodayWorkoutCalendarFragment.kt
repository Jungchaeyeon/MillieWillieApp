package com.makeus.milliewillie.ui.todayWorkout

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentTodayWorkoutCalendarBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.model.TodayRoutines
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class TodayWorkoutCalendarFragment: BaseDataBindingFragment<FragmentTodayWorkoutCalendarBinding>(R.layout.fragment_today_workout_calendar) {

    private val viewModel by viewModel<TodayWorkoutViewModel>()


    override fun FragmentTodayWorkoutCalendarBinding.onBind() {
        vi = this@TodayWorkoutCalendarFragment
        viParent = TodayWorkoutActivity()
        vm = viewModel

        todayDate()

        binding.todayCalendarImgGotoFeed.setOnClickListener {
            (activity as TodayWorkoutActivity).onClickViewChange(2)
        }

        binding.todayCalendarRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<TodayRoutines>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<TodayRoutines, WorkoutRoutineRecyclerItemBinding>(R.layout.workout_routine_recycler_item){
                        viCalendar = this@TodayWorkoutCalendarFragment
                        item = it
                    }
                )
        }

    }

    fun itemClick() {

    }

    fun todayDate(){
        val dateInstance = Calendar.getInstance()

        val month = dateInstance.get(Calendar.MONTH)
        val day = dateInstance.get(Calendar.DAY_OF_MONTH)
        val dayOfWeek = dateInstance.get(Calendar.DAY_OF_WEEK)
        var dayOfWeekText = ""

        when (dayOfWeek) {
            1 -> dayOfWeekText = "일"
            2 -> dayOfWeekText = "월"
            3 -> dayOfWeekText = "화"
            4 -> dayOfWeekText = "수"
            5 -> dayOfWeekText = "목"
            6 -> dayOfWeekText = "금"
            7 -> dayOfWeekText = "토"
        }

        val today = String.format(getString(R.string.todayDateForm, month, day, dayOfWeekText))

        viewModel.liveDataToday.postValue(today)
        Log.e(today)
    }

    override fun onResume() {
        super.onResume()
        viewModel.defaultRoutineItemList()
    }

}