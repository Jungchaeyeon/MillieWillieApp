package com.makeus.milliewillie.ui.todayWorkout

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.calendar.DotDecorator
import com.makeus.milliewillie.calendar.EventDecorator
import com.makeus.milliewillie.calendar.SelectionDecorator
import com.makeus.milliewillie.calendar.SundayDecorator
import com.makeus.milliewillie.databinding.FragmentTodayWorkoutCalendarBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.model.MyRoutineInfo
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.EXERCISE_ID
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.CalendarUtils.getYear
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList


class TodayWorkoutCalendarFragment: BaseDataBindingFragment<FragmentTodayWorkoutCalendarBinding>(R.layout.fragment_today_workout_calendar) {

    private val viewModel by viewModel<TodayWorkoutViewModel>()

    val now = Date()
    val format = SimpleDateFormat("yyyy-MM-dd")
    val date = format.format(now)

    var year = Calendar.getInstance().get(Calendar.YEAR)
    var month = Calendar.getInstance().get(Calendar.MONTH) + 1

    var calendarItemYear = ""
    var calendarItemMonth = ""
    var calendarItemDay = ""

    val calendarMonthList = ArrayList<CalendarDay>()
    var reportsDatesList = ArrayList<String>()

    private val routineArray = ArrayList<MyRoutineInfo>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun FragmentTodayWorkoutCalendarBinding.onBind() {
        vi = this@TodayWorkoutCalendarFragment
        viParent = TodayWorkoutActivity()
        vm = viewModel

        executeGetRoutines()

        todayDate()

        val calendarDayList: ArrayList<CalendarDay> = ArrayList()

        binding.calendar.apply {
            state().edit()
                .isCacheCalendarPositionEnabled(false)
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit()
            isDynamicHeightEnabled = true
            setPadding(0,-10, 0, 0)
        }


        binding.calendar.setOnMonthChangedListener { widget, date ->
            year = date.year
            month = date.month + 1
            calendarMonthList.clear()
            executeGetReports()
        }

        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            if (date !in calendarDayList) {
                binding.calendar.apply {
                    addDecorators(SundayDecorator(), SelectionDecorator(date, context!!))
                    invalidateDecorators()
                }
            }
        }

        binding.todayCalendarImgGotoFeed.setOnClickListener {
            (activity as TodayWorkoutActivity).onClickViewChange(2)
        }

        binding.todayCalendarRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<MyRoutineInfo>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MyRoutineInfo, WorkoutRoutineRecyclerItemBinding>(
                        R.layout.workout_routine_recycler_item
                    ) {
                        viCalendar = this@TodayWorkoutCalendarFragment
                        item = it
                    }
                )
        }

    }

    fun setDateFormat(date: String) {
        var idx = 0
        calendarItemYear = ""
        calendarItemMonth = ""
        calendarItemDay = ""
        Log.e(date)
        for (i in date.indices) {
            if (i == 0 || i == date.length-1) continue
            else if (date[i] == '-') idx++
            else if (idx < 1) calendarItemYear += date[i]
            else if (idx < 2) calendarItemMonth += date[i]
            else calendarItemDay += date[i]
        }

    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun executeGetRoutines() {
        viewModel.apiRepository.getRoutines(
            path = SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong(),
            targetDate = date
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


    fun executeGetReports() {
        reportsDatesList.clear()
        viewModel.apiRepository.getReports(
            SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong(),
            viewYear = year, viewMonth = month)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("getReports 호출 성공")
                    for (i in 0 until it.result.size()) reportsDatesList.add(it.result[i].toString())
                    Log.e("reportsDatesList = $reportsDatesList")

                    for (i in 0 until reportsDatesList.size) {
                        setDateFormat(reportsDatesList[i])
                        calendarMonthList.add(CalendarDay.from(calendarItemYear.toInt(), calendarItemMonth.toInt()-1, calendarItemDay.toInt()))
                    }
                    calendarMonthList.forEach {
                        binding.calendar.apply {
                            removeDecorators()
                            addDecorators(SundayDecorator(), DotDecorator(R.color.blue_green, calendarMonthList, context!!))
                            invalidateDecorators()
                        }
                    }
                    Log.e("calendarMonthList = ${calendarMonthList}")
                } else {
                    Log.e("getReports 호출 실패")
                    Log.e(it.message)
                }


            }.disposeOnDestroy(this)



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

        val today = String.format(getString(R.string.todayDateForm, month + 1, day, dayOfWeekText))

        viewModel.liveDataToday.postValue(today)
        Log.e(today)
    }

    fun onClickCancel() {
        activity?.onBackPressed()
    }



    override fun onResume() {
        super.onResume()
        executeGetReports()

        viewModel.defaultRoutineItemList()
    }

}