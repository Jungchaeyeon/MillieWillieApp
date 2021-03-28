package com.makeus.milliewillie.ui.report

import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityReportBinding
import com.makeus.milliewillie.databinding.ActivityReportRecyclerItemBinding
import com.makeus.milliewillie.databinding.WorkoutRoutineRecyclerItemBinding
import com.makeus.milliewillie.model.MyRoutineInfo
import com.makeus.milliewillie.model.ReportRecyclerItem
import com.makeus.milliewillie.ui.home.tab2.WorkoutFragment.Companion.EXERCISE_ID
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartActivity.Companion.REPORT_DATE_KEY
import com.makeus.milliewillie.ui.workoutStart.WorkoutStartActivity.Companion.START_ROUTINE_ID
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

class ReportActivity: BaseDataBindingActivity<ActivityReportBinding>(R.layout.activity_report) {

    private val viewModel by viewModel<ReportViewModel>()

    private var routineId by Delegates.notNull<Long>()
    private lateinit var reportDate: String

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


                } else {
                    Log.e("getReports 호출 실패")
                    Log.e(it.message)
                }
            }.disposeOnDestroy(this)
    }


}