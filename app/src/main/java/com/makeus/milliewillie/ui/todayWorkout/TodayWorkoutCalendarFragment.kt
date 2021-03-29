package com.makeus.milliewillie.ui.todayWorkout

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.calendar.DotDecorator
import com.makeus.milliewillie.calendar.SelectionDecorator
import com.makeus.milliewillie.calendar.SundayDecorator
import com.makeus.milliewillie.databinding.FragmentTodayWorkoutCalendarBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.model.MyRoutineInfo
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.EXERCISE_ID
import com.makeus.milliewillie.ui.home.tab2.adapter.WorkoutRoutineAdapter
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartActivity
import com.makeus.milliewillie.util.BasicTextFormat
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class TodayWorkoutCalendarFragment: BaseDataBindingFragment<FragmentTodayWorkoutCalendarBinding>(R.layout.fragment_today_workout_calendar) {

    private val viewModel by viewModel<TodayWorkoutViewModel>()

    private val now = Date()
    val format = SimpleDateFormat("yyyy-MM-dd")
    var date: String = format.format(now)

    var year = Calendar.getInstance().get(Calendar.YEAR)
    var month = Calendar.getInstance().get(Calendar.MONTH) + 1
    var day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

    var calendarItemYear = ""
    var calendarItemMonth = ""
    var calendarItemDay = ""

    val calendarMonthList = ArrayList<CalendarDay>()
    var reportsDatesList = ArrayList<String>()

    private lateinit var calRoutineRecyclerAdapter: WorkoutRoutineAdapter
    private var position by Delegates.notNull<Int>()
    private lateinit var reportDate: String
    private val calendar = Calendar.getInstance()
    private val routineArray = ArrayList<MyRoutineInfo>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun FragmentTodayWorkoutCalendarBinding.onBind() {
        vi = this@TodayWorkoutCalendarFragment
        viParent = TodayWorkoutActivity()
        vm = viewModel

        reportDate = BasicTextFormat.BasicDateFormat(
            this@TodayWorkoutCalendarFragment.calendar.get(Calendar.YEAR).toString(),
            (this@TodayWorkoutCalendarFragment.calendar.get(Calendar.MONTH)+1).toString(),
            this@TodayWorkoutCalendarFragment.calendar.get(Calendar.DAY_OF_MONTH).toString()
        )

        val dateInstance = Calendar.getInstance()
        todayDate(dateInstance.get(Calendar.MONTH), dateInstance.get(Calendar.DAY_OF_MONTH))

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
            executeGetCalendarReports()
        }

        binding.calendar.setOnDateChangedListener { widget, date, selected ->
            year = date.year
            month = date.month + 1
            day = date.day
            this@TodayWorkoutCalendarFragment.date = BasicTextFormat.BasicDateFormat(year.toString(), month.toString(), day.toString())
            todayDate(month-1, day)
            executeGetRoutines()
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

//        binding.todayCalendarRecycler.run {
//            adapter = BaseDataBindingRecyclerViewAdapter<MyRoutineInfo>()
//                .addViewType(
//                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MyRoutineInfo, WorkoutRoutineRecyclerItemBinding>(
//                        R.layout.workout_routine_recycler_item
//                    ) {
//                        viCalendar = this@TodayWorkoutCalendarFragment
//                        item = it
//                    }
//                )
//        }

    }

    fun setRecyclerAdapter() {
        Log.e("routineItemList = $routineArray")
        calRoutineRecyclerAdapter = WorkoutRoutineAdapter(context, routineArray)
        binding.todayCalendarRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)

            // 키워드 아이템 클릭 리스너
            calRoutineRecyclerAdapter.let {
                it.setRoutineItemClickListener(object : WorkoutRoutineAdapter.RoutineItemClickListener {
                    override fun onItemClick(position: Int) {
                        Log.e(position.toString())
                        this@TodayWorkoutCalendarFragment.position = position

                        onClickRoutine()
                    }
                })
            } // end listener
            adapter = calRoutineRecyclerAdapter
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
        routineArray.clear()
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
                                routineId = item.get("routineId").asLong,
                                isDoneRoutine = item.get("isDoneRoutine").asString
                            )
                        )
                    }
                    Log.e("routineArray = $routineArray")

                } else {
                    Log.e("getRoutines 호출 실패")
                    Log.e(it.message)
                }
                setRecyclerAdapter()
            }.disposeOnDestroy(this)
    }


    private fun executeGetCalendarReports() {
        reportsDatesList.clear()
        viewModel.apiRepository.getCalendarReports(
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
                    Log.e("calendarMonthList = $calendarMonthList")
                } else {
                    Log.e("getReports 호출 실패")
                    Log.e(it.message)
                }


            }.disposeOnDestroy(this)



    }

    private fun todayDate(month: Int, day: Int){
        val todayMonth = Calendar.getInstance().get(Calendar.MONTH)
        val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        if (month == todayMonth && day == todayDay) binding.todayCalendarTextTodayDateOfWeek.visibility = View.VISIBLE
        else binding.todayCalendarTextTodayDateOfWeek.visibility = View.GONE

        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        var dayOfWeekText = ""

//        val difDayOfWeek = if (todayDay > day) (todayDay - day) % 7 else (day - todayDay) % 7
//        var difIntDayOfWeek = 0
////        if (difDayOfWeek > 7) difDayOfWeek -= 7
//
//        Log.e("dayOfWeek = $dayOfWeek")
//        Log.e("difDayOfWeek = $difDayOfWeek")
//
//        when (difDayOfWeek) {
//            0 -> difIntDayOfWeek = dayOfWeek
//            1 -> {
//                difIntDayOfWeek = if (todayDay > day) dayOfWeek - 1 else dayOfWeek +1
//            }
//        }

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

    fun onClickRoutine() {
        when (routineArray[position].isDoneRoutine.toBoolean()) {
            true -> { // 운동 리포트 화면
                ActivityNavigator.with(this).reports().apply {
                    putExtra(WorkoutStartActivity.START_ROUTINE_ID, routineArray[position].routineId)
                    putExtra(WorkoutStartActivity.REPORT_DATE_KEY, reportDate)
                    start()
                }
            }
            false -> { // 운동 시작 화면
                ActivityNavigator.with(this).workoutStart().apply {
                    putExtra(WorkoutFragment.ROUTINE_ID_KEY_FROM_WORKOUT, routineArray[position].routineId)
                    WorkoutFragment.isModifiedRoutine = true
                    start()
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        executeGetCalendarReports()

        executeGetRoutines()
    }

}