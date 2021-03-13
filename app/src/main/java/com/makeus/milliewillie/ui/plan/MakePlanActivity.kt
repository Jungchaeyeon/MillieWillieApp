package com.makeus.milliewillie.ui.plan

import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.ext.BgTint
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.Plan
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.activity_make_plan.rv_memo_list
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.view.*
import kotlinx.android.synthetic.main.item_plan_layout.*
import kotlinx.android.synthetic.main.item_plan_layout.btn_no_notice
import kotlinx.android.synthetic.main.item_plan_layout.view.*
import kotlinx.android.synthetic.main.item_plan_todo.*
import kotlinx.android.synthetic.main.item_plan_todo.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class MakePlanActivity :
    BaseDataBindingActivity<ActivityMakePlanBinding>(R.layout.activity_make_plan) {

    private val viewModel by viewModel<MakePlanViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    companion object {
        fun getInstance() = MakePlanActivity()
    }

    override fun ActivityMakePlanBinding.onBind() {
        vi = this@MakePlanActivity
        vm = viewModel
        viewModel.bindLifecycle(this@MakePlanActivity)

        rv_memo_list.run {
            adapter = BaseDataBindingRecyclerViewAdapter<Plan.Todos>()
                .setItemViewType { item, position, isLast ->
                    if (position == 0) 0 else 1
                }
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Plan.Todos, ItemPlanLayoutBinding>(
                        R.layout.item_plan_layout
                    ) {
                        vi = this@MakePlanActivity
                        vm = viewModel
                    })

                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Plan.Todos, ItemPlanTodoBinding>(
                        R.layout.item_plan_todo
                    ) {
                        vi = this@MakePlanActivity
                        btn_no_notice.isChecked = true
                        item = it
                    })

        }
    }


    fun onClickPlanType() {
        PlanTypeBottomSheetDialogFragment.getInstance()
            .setOnClickDate {
                val type = repositoryCached.getPlanType()
                btn_tp.text = type
                when (type) {
                    "정기휴가", "일정" -> {
                        layout_other_plan.visibility = View.GONE
                        layout.visibility = View.VISIBLE
                        item_todo.visibility = View.VISIBLE
                        when(type) {
                            "정기휴가" -> {
                                layout_notice_week.visibility = View.GONE
                                layout_leave.visibility = View.VISIBLE
                            }
                            "일정" -> {
                                layout_notice_week.visibility = View.VISIBLE
                                layout_leave.visibility = View.GONE
                            }
                        }
                    }
                    "포상휴가", "외박", "훈련", "면회", "외출", "전투휴무", "당직" -> {
                        layout_other_plan.visibility = View.VISIBLE
                        layout_notice_week.visibility = View.GONE
                        layout_leave.visibility = View.GONE

                        when(type) {
                            "외박" -> {
                                title_day_and_night.text = "외박"
                                item_todo.visibility = View.VISIBLE
                            }
                            "훈련" -> {
                                title_day_and_night.text = "훈련"
                                title_todo.visibility = View.GONE
                            }
                            "외출" -> {
                                title_day_and_night.text = "외출"
                                title_todo.visibility = View.GONE
                            }
                            "면회" -> {
                                title_day_and_night.text = "면회"
                                title_todo.visibility = View.VISIBLE
                            }
                            "전투휴무" -> {
                                title_day_and_night.text = "전투휴무"
                                title_todo.visibility = View.GONE
                            }
                            "당직" -> {
                                title_day_and_night.text = "당직"
                                title_todo.visibility = View.GONE
                            }
                        }


                    }
                    else -> {}}
                }.show(supportFragmentManager)

            }

        fun onClickColor() {

            ColorPickerBottomSheetFragment.getInstance()
                .setOnClickColor {
                    btn_color.BgTint(it)
                }
                .show(supportFragmentManager)
        }

        fun onClickCalendar() {
            ActivityNavigator.with(this).plancalendar().start()

        }

        fun onClickDate(view: View) {
            if (view.id == R.id.btn_no_notice) {
                if (btn_no_notice.isChecked) {
                    btn_mon.isChecked = false
                    btn_tue.isChecked = false
                    btn_wed.isChecked = false
                    btn_thur.isChecked = false
                    btn_fri.isChecked = false
                    btn_sat.isChecked = false
                    btn_sun.isChecked = false
                }
            } else {
                btn_no_notice.isChecked = false
            }
        }

        fun onClickNotice() {
            if (btn_notice.isChecked) repositoryCached.setValue(LocalKey.PLANNOTICE, "Y")
            else {
                repositoryCached.setValue(LocalKey.PLANNOTICE, "N")
            }
            repositoryCached.getNotice().toString().showShortToastSafe()
        }

        override fun onResume() {
            super.onResume()
            viewModel.requestPlanTodoList()
        }
    }




