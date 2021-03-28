package com.makeus.milliewillie.ui.report

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityReportBinding
import com.makeus.milliewillie.databinding.ActivityReportRecyclerItemBinding
import com.makeus.milliewillie.model.ReportInnerRecyclerItem
import com.makeus.milliewillie.model.ReportRecyclerItem
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.EXERCISE_ID
import com.makeus.milliewillie.ui.report.adapter.ReportsAdapter
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartActivity.Companion.REPORT_DATE_KEY
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartActivity.Companion.START_ROUTINE_ID
import com.makeus.milliewillie.util.BasicTextFormat
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class ReportActivity: BaseDataBindingActivity<ActivityReportBinding>(R.layout.activity_report) {

    private val viewModel by viewModel<ReportViewModel>()

    private var routineId by Delegates.notNull<Long>()
    private lateinit var reportDate: String

    private lateinit var reportsAdapter: ReportsAdapter
    val outterList = ArrayList<ReportRecyclerItem>()

    override fun ActivityReportBinding.onBind() {
        vi = this@ReportActivity
        vm = viewModel

        if (intent.hasExtra(START_ROUTINE_ID)) {
            routineId = intent.getLongExtra(START_ROUTINE_ID,0)
        }
        if (intent.hasExtra(REPORT_DATE_KEY)) {
            reportDate = intent.getStringExtra(REPORT_DATE_KEY).toString()
        }

        executeGetReports()

        binding.reportRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<ReportRecyclerItem>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<ReportRecyclerItem, ActivityReportRecyclerItemBinding>(R.layout.activity_report_recycler_item) {
                        vi = this@ReportActivity
                        item = it
                    }
                )
        }





    }


    private fun executeGetReports() {
        viewModel.apiRepository.getReports(exerciseId = SharedPreference.getSettingItem(EXERCISE_ID)!!.toLong(),
        routineId = routineId, reportDate = reportDate)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("getReports 호출 성공")

                    binding.reportTextTotalTime.text = it.result.totalTime
                    binding.reportTextReportDate.text = it.result.reportDate
                    binding.reportEditContent.setText(it.result.reportText)

                    it.result.exerciseList.forEach { routine ->
                        val innerList = ArrayList<ReportInnerRecyclerItem>()

                        val exerciseName = routine.exerciseName
                        val exerciseStatus = routine.exerciseStatus
                        val doneSet = routine.doneSet
                        val isDone = routine.isDone

                        var idx = 0
                        var detailText = ""
                        var lastSetIndex = 0
                        routine.setList.forEach { detail ->
                            val setCount = "${detail.setCount}set"
                            val weight = detail.weight
                            val count = detail.count
                            val time = detail.time

                            detailText = BasicTextFormat.BasicRoutineOptionTextForm(setCount, weight, count, time, idx)

                            idx++
                            lastSetIndex = detail.setCount
                        }
                        Log.e("lastSetIndex = $lastSetIndex")
                        Log.e("doneSet = $doneSet")

                        for (i in 0 until lastSetIndex) {
                            if (doneSet >= i) innerList.add(ReportInnerRecyclerItem(circle = R.drawable.one_small_blu))
                            else innerList.add(ReportInnerRecyclerItem(circle = R.drawable.one_small_grey))
                        }
                        Log.e("innerList = $innerList")


                        val outterItem = ReportRecyclerItem(exerciseName = exerciseName,exerciseStatus = exerciseStatus, exerciseOption = detailText,
                            doneList = innerList, isDone = isDone, doneSet = doneSet)
                        outterList.add(outterItem)
                    }


                } else {
                    Log.e("getReports 호출 실패")
                    Log.e(it.message)
                }

                setRecyclerAdapter()
            }.disposeOnDestroy(this)
    }

    fun setRecyclerAdapter() {
        Log.e("outterList = $outterList")
        reportsAdapter = ReportsAdapter(this, outterList)
        binding.reportRecycler.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)

            // 키워드 아이템 클릭 리스너
            reportsAdapter.let {
                it.setReportsItemClickListener(object : ReportsAdapter.ReportsItemClickListener {
                    override fun onItemClick(position: Int) {
                        Log.e(position.toString())

                    }
                })
            } // end listener
            adapter = reportsAdapter
        }

    }

    var isFold = false
    fun onClickToFold() {
        isFold = !isFold
        when (isFold) {
            true -> binding.reportRecycler.visibility = View.VISIBLE
            false -> binding.reportRecycler.visibility = View.GONE
        }

    }

    fun onClickBack() {
        onBackPressed()
    }


}
