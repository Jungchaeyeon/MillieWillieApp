package com.makeus.milliewillie.ui.todayWorkout

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentTodayWorkoutCalendarBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.model.MyRoutineInfo
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TodayWorkoutCalendarFragment: BaseDataBindingFragment<FragmentTodayWorkoutCalendarBinding>(R.layout.fragment_today_workout_calendar) {

    private val viewModel by viewModel<TodayWorkoutViewModel>()

    val now = Date()
    val format = SimpleDateFormat("yyyy-MM-dd")
    val date = format.format(now)

    private val routineArray = ArrayList<MyRoutineInfo>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun FragmentTodayWorkoutCalendarBinding.onBind() {
        vi = this@TodayWorkoutCalendarFragment
        viParent = TodayWorkoutActivity()
        vm = viewModel

        executeGetRoutines()

        todayDate()

        binding.todayCalendarImgGotoFeed.setOnClickListener {
            (activity as TodayWorkoutActivity).onClickViewChange(2)
        }

        binding.todayCalendarRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<MyRoutineInfo>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MyRoutineInfo, WorkoutRoutineRecyclerItemBinding>(R.layout.workout_routine_recycler_item){
                        viCalendar = this@TodayWorkoutCalendarFragment
                        item = it
                    }
                )
        }

    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun executeGetRoutines() {
        viewModel.apiRepository.getRoutines(
            path = SharedPreference.getSettingItem(WorkoutFragment.EXERCISE_ID)!!.toLong(), targetDate = date
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess){
                    Log.e("getRoutines 호출 성공")

                    it.result.asJsonArray.forEach { objects ->
                        val item = objects.asJsonObject

                        routineArray.add(
                            MyRoutineInfo(
                                routineName = item.get("routineName").asString,
                                routineRepeatDay = item.get("routineRepeatDay").asString,
                                routineId = item.get("routineId").asLong
                            )
                        )
                    }
                    viewModel.createRoutineItem(routineArray)
                } else {
                    Log.e("getRoutines 호출 실패")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this)
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