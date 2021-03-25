package com.makeus.milliewillie.ui

import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMainCalendarviewBinding
import com.makeus.milliewillie.databinding.ItemMainScheduleBinding
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.ui.plan.MakePlanViewModel
import kotlinx.android.synthetic.main.activity_main_calendarview.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.item_home_layout.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainCalendarActivity :
    BaseDataBindingActivity<ActivityMainCalendarviewBinding>(R.layout.activity_main_calendarview) {

    val viewModel by viewModel<MakePlanViewModel>()

    companion object {
        fun getInstance() = MainCalendarActivity()
    }

    override fun ActivityMainCalendarviewBinding.onBind() {
        vm = viewModel
        rv_cal_list.run {

            adapter = BaseDataBindingRecyclerViewAdapter<MainSchedule>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MainSchedule, ItemMainScheduleBinding>(
                        R.layout.item_main_schedule
                    ) {
                        if (this@MainCalendarActivity.viewModel.planItems.size >= 2) {
                            txtBlank.visibility = View.GONE
                        }
                        item = it
                    })

        }
    }


}