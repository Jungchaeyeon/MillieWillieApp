package com.makeusteam.milliewillie.ui.todayWorkout

import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.fragment.BaseDataBindingFragment
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.MyApplication.Companion.ROUTINE_ID_KEY_FROM_WORKOUT
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.calendar.DotDecorator
import com.makeusteam.milliewillie.calendar.SelectionDecorator
import com.makeusteam.milliewillie.calendar.SundayDecorator
import com.makeusteam.milliewillie.databinding.FragmentTodayWorkoutCalendarBinding
import com.makeusteam.milliewillie.model.MyRoutineInfo
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.home.tab2.WorkoutFragment
import com.makeusteam.milliewillie.ui.home.tab2.adapter.WorkoutRoutineAdapter
import com.makeusteam.milliewillie.ui.workoutStart.WorkoutStartActivity
import com.makeusteam.milliewillie.util.BasicTextFormat
import com.makeusteam.milliewillie.util.Log
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class TodayWorkoutCalendarFragment: BaseDataBindingFragment<FragmentTodayWorkoutCalendarBinding>(R.layout.fragment_today_workout_calendar) {

    private val viewModel by viewModel<TodayWorkoutViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

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
            addDecorator(SundayDecorator())
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

            // ????????? ????????? ?????? ?????????
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
            path = repositoryCached.getExerciseId(),
            targetDate = date
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess){
                    Log.e("getRoutines ?????? ??????")

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
                    Log.e("getRoutines ?????? ??????")
                    Log.e(it.message)
                }
                setRecyclerAdapter()
            }.disposeOnDestroy(this)
    }


    private fun executeGetCalendarReports() {
        reportsDatesList.clear()
        viewModel.apiRepository.getCalendarReports(
            path = repositoryCached.getExerciseId(),
            viewYear = year, viewMonth = month)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("getReports ?????? ??????")
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
                    Log.e("getReports ?????? ??????")
                    Log.e(it.message)
                }


            }.disposeOnDestroy(this)



    }

    private fun todayDate(month: Int, day: Int){
        val todayMonth = Calendar.getInstance().get(Calendar.MONTH)
        val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        if (month == todayMonth && day == todayDay) binding.todayCalendarTextTodayDateOfWeek.visibility = View.VISIBLE
        else binding.todayCalendarTextTodayDateOfWeek.visibility = View.GONE

//        val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
//        var dayOfWeekText = ""

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

//        when (dayOfWeek) {
//            1 -> dayOfWeekText = "???"
//            2 -> dayOfWeekText = "???"
//            3 -> dayOfWeekText = "???"
//            4 -> dayOfWeekText = "???"
//            5 -> dayOfWeekText = "???"
//            6 -> dayOfWeekText = "???"
//            7 -> dayOfWeekText = "???"
//        }

        val today = String.format(getString(R.string.todayDateForm, month + 1, day))

        viewModel.liveDataToday.postValue(today)
        Log.e(today)
    }

    fun onClickCancel() {
        activity?.onBackPressed()
    }

    fun onClickRoutine() {
        when (routineArray[position].isDoneRoutine.toBoolean()) {
            true -> { // ?????? ????????? ??????
                ActivityNavigator.with(this).reports().apply {
                    putExtra(WorkoutStartActivity.START_ROUTINE_ID, routineArray[position].routineId)
                    putExtra(WorkoutStartActivity.REPORT_DATE_KEY, reportDate)
                    start()
                }
            }
            false -> { // ?????? ?????? ??????
                ActivityNavigator.with(this).workoutStart().apply {
                    putExtra(ROUTINE_ID_KEY_FROM_WORKOUT, routineArray[position].routineId)
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