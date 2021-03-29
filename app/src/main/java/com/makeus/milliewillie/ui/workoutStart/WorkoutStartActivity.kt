package com.makeus.milliewillie.ui.workoutStart

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityWorkoutStartBinding
import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.ui.common.DialogWorkoutDoneFragment
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.EXERCISE_ID
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.ROUTINE_ID_KEY_FROM_WORKOUT
import com.makeus.milliewillie.ui.workoutStart.adapter.WorkoutStartAdapter
import com.makeus.milliewillie.util.BasicTextFormat
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.timer
import kotlin.properties.Delegates

class WorkoutStartActivity: BaseDataBindingActivity<ActivityWorkoutStartBinding>(R.layout.activity_workout_start) {

    private val viewModel by viewModel<WorkoutStartViewModel>()
    lateinit var timer: Timer

    private var position by Delegates.notNull<Int>()

    lateinit var startRecyclerAdapter: WorkoutStartAdapter
    private var routineId by Delegates.notNull<Long>()

    var startItemList = ArrayList<StartRecyclerItem>()
    val innerCircleItemList = ArrayList<StartRecyclerCircleItem>()

    private lateinit var today: String
    private lateinit var totalTime: String
    private lateinit var exerciseStatus: String
    var statusList = ArrayList<Int>()

    companion object {
        const val REPORT_DATE_KEY = "REPORT_DATE_KEY"
        const val START_ROUTINE_ID = "START_ROUTINE_ID"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WorkoutStartFirstBottomSheetFragment.getInstance()
            .setOnClickOk {
                stopWatch()
            }.show(supportFragmentManager)
    }

    override fun onResume() {
        super.onResume()

    }


    override fun ActivityWorkoutStartBinding.onBind() {
        vi = this@WorkoutStartActivity
        vm = viewModel

        val calendar = Calendar.getInstance()

        today = BasicTextFormat.BasicDateFormat(
            calendar.get(Calendar.YEAR).toString(),
            (calendar.get(Calendar.MONTH)+1).toString(),
            calendar.get(Calendar.DAY_OF_MONTH).toString()
        )

        if (intent.hasExtra(ROUTINE_ID_KEY_FROM_WORKOUT)) {
            routineId = intent.getLongExtra(ROUTINE_ID_KEY_FROM_WORKOUT, 0)
        }
        Log.e("routineId = $routineId")

        executeGetStartExercises()


    }

    private fun executePostReports() {
        exerciseStatus = ""
        for (i in 0 until statusList.size) {
            if (i == 0) exerciseStatus += statusList[i].toString()
            else exerciseStatus += "#${statusList[i]}"
        }
        Log.e("exerciseStatus = $exerciseStatus")

        viewModel.apiRepository.postReports(
            exerciseId = SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong(),
            routineId = routineId, body = PostReportsRequest(routineId = routineId, totalTime = totalTime, exerciseStatus = exerciseStatus)
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("postReports 호출 설공")

                } else {
                    Log.e("postReports 호출 실패")
                    Log.e(it.message)
                }

                ActivityNavigator.with(this).reports().apply {
                    Log.e("routineId = $routineId")
                    putExtra(START_ROUTINE_ID, routineId)
                    Log.e("today = $today")
                    putExtra(REPORT_DATE_KEY, today)
                    start()
                }
                finish()
            }.disposeOnDestroy(this)
    }

    private var routineName = ""
    private var repeatDay = ""

    private var exerciseName = ""
    private var setCount = 0
    private var weight = ""
    private var count = ""
    private var time = ""

    fun executeGetStartExercises() {
        viewModel.apiRepository.getStartExercises(
            exerciseId = SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong(),
            routineId = routineId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("getStartExercises 호출 성공")
                    Log.e(it.result.toString())
                    routineName = it.result.routineName
                    repeatDay = it.result.repeatDay
                    binding.startTextRoutineName.text = routineName
                    binding.startTextRoutineDate.text = repeatDay
                    it.result.exerciseList.forEach { detailInfo ->
                        var idx = 0
                        exerciseName = detailInfo.exerciseName
                        var textForm = ""
                        detailInfo.setInfoList.forEach {inner ->
                            setCount = inner.setCount
                            if (inner.weight != "-1") weight = inner.weight
                            if (inner.count != "-1") count = inner.count
                            if (inner.time != "-1") time = inner.time

                            if (idx == 0) textForm = "${setCount}set $weight $count $time"
                            else textForm = "\n${setCount}set $weight $count $time"

                            idx ++
                        }
                        val tempList = ArrayList<StartRecyclerCircleItem>()
                        for (i in 0 until setCount) tempList.add(StartRecyclerCircleItem(circle = R.drawable.one_currnet_gray))

                        val recyclerItem = StartRecyclerItem(exName = exerciseName, exInfo = textForm, circleList = tempList)
                        startItemList.add(recyclerItem)
                        Log.e(startItemList.toString())
                    }


                } else {
                    Log.e("getStartExercises 호출 실패")
                    Log.e(it.message)
                }
                setRecyclerAdapter()
            }.disposeOnDestroy(this)

    }

    fun setRecyclerAdapter() {
//        startItemList = viewModel.createRecycler()
        Log.e("startItemList = $startItemList")
        startRecyclerAdapter = WorkoutStartAdapter(this, startItemList, viewModel)
        binding.startRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)

            // 키워드 아이템 클릭 리스너
            startRecyclerAdapter.let {
                it.setStartItemClickListener(object : WorkoutStartAdapter.StartItemClickListener {
                    override fun onItemClick(position: Int) {
                        Log.e(position.toString())

                    }
                })
            } // end listener
            adapter = startRecyclerAdapter
        }

    }

    var isStart = false
    var hour: String = "00"
    var min: String = "00"
    var sec: String = "00"
    fun stopWatch() {
        isStart = !isStart
        when (isStart) {
            true -> {
                binding.startBtnStart.text = getString(R.string.stop)
                viewModel.liveDataTimeSec.postValue(sec)
                viewModel.liveDataTimeMin.postValue(min)
                viewModel.liveDataTimeHour.postValue(hour)
                timer = timer(period = 1000, initialDelay = 1000) {
                    Handler(Looper.getMainLooper()).post {
                        viewModel.increaseSec(viewModel.liveDataTimeSec.value!!)
                        if (viewModel.liveDataTimeSec.value!!.toInt() == 59) {
                            viewModel.liveDataTimeSec.postValue("00")
                            viewModel.increaseMin(viewModel.liveDataTimeMin.value!!)
                            binding.startTextMinute.text = viewModel.liveDataTimeMin.value.toString()
                        }
                        if (viewModel.liveDataTimeMin.value!!.toInt() == 59) {
                            viewModel.liveDataTimeMin.postValue("00")
                            viewModel.increaseHour(viewModel.liveDataTimeHour.value!!)
                            binding.startTextHour.text = viewModel.liveDataTimeHour.value.toString()
                        }
                        binding.startTextSec.text = viewModel.liveDataTimeSec.toString()
                        totalTime = BasicTextFormat.BasicTotalTimeFormat(viewModel.liveDataTimeHour.value!!, viewModel.liveDataTimeMin.value!!, viewModel.liveDataTimeSec.value!!)
                    }
                }
            }
            false -> {
                binding.startBtnStart.text = getString(R.string.start)
                hour = viewModel.liveDataTimeHour.value!!
                min = viewModel.liveDataTimeMin.value!!
                sec = viewModel.liveDataTimeSec.value!!
                totalTime = BasicTextFormat.BasicTotalTimeFormat(hour, min, sec)
                timer.cancel()
            }
        }

    }

    fun onClickDone() {
        DialogWorkoutDoneFragment.getInstance()
            .setTitle(getString(R.string.is_done_ex))
            .setOnClickOk ({
                startItemList.forEach {
                    var count = 0
                    for (i in 0 until it.circleList.size) {
                        if (it.circleList[i].circle == R.drawable.one_currnet_blue) count++
                    }
                    statusList.add(count)
                }

                executePostReports()

            }, "start")
            .show(supportFragmentManager)
    }

    fun onClickBack() {
        onBackPressed()
    }

}