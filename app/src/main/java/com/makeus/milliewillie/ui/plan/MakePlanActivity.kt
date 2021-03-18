package com.makeus.milliewillie.ui.plan

import android.graphics.Color
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.ext.bgTint
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.model.Plan
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.activity_my_page_edit.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.view.*
import kotlinx.android.synthetic.main.item_plan_todo.*
import kotlinx.android.synthetic.main.item_plan_todo.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class MakePlanActivity :
    BaseDataBindingActivity<ActivityMakePlanBinding>(R.layout.activity_make_plan) {

    private val viewModel by viewModel<MakePlanViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    val context = this

    companion object {
        fun getInstance() = MakePlanActivity()
    }

    override fun ActivityMakePlanBinding.onBind() {
        vi = this@MakePlanActivity
        vm = viewModel
        viewModel.bindLifecycle(this@MakePlanActivity)

        rv_memo_list.isNestedScrollingEnabled = false
        rv_memo_list.run {
            adapter = BaseDataBindingRecyclerViewAdapter<Plan.Todos>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Plan.Todos, ItemPlanTodoBinding>(
                        R.layout.item_plan_todo
                    ) {
                        vi = this@MakePlanActivity
                        item = it
                    })
        }
        viewModel.liveDayAndNight.observe(
            this@MakePlanActivity,
            androidx.lifecycle.Observer { txt_daynight.text = it })
    }

    fun onClickPlanType() {
        viewModel.liveDate.postValue("")
        viewModel.liveDayAndNight.postValue("")
        PlanTypeBottomSheetDialogFragment.getInstance()
            .setOnClickDate {
                val type = repositoryCached.getPlanType()
                btn_tp.text = type
                when (type) {
                    "정기휴가", "일정" -> {
                        layout_other_plan.visibility = View.GONE
                        layout_mk_plan.visibility = View.VISIBLE
                        item_todo.visibility = View.VISIBLE
                        when (type) {
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

                        when (type) {
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
                    else -> {
                    }
                }
            }.show(supportFragmentManager)

    }

    fun onClickColor() {
        ColorPickerBottomSheetFragment.getInstance()
            .setOnClickColor {
                btn_color.bgTint(it)
                viewModel.livePlanColor.postValue(it)
            }
            .show(supportFragmentManager)
    }

    fun onClickCalendar() {
        ActivityNavigator.with(this).plancalendar().start()
        txt_daynight.setTextColor(Color.parseColor("#3e3e3e"))
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
        if (btn_notice.isChecked) {
            //repositoryCached.setValue(LocalKey.PLANNOTICE, "Y")
            txt_notice.setTextColor(Color.parseColor("#3e3e3e"))
        } else {
            //repositoryCached.setValue(LocalKey.PLANNOTICE, "N")
            txt_notice.setTextColor(Color.parseColor("#9d9d9d"))
        }
    }

    fun onClickDone() {
        if (plan_title.text.isEmpty()) {
            Snackbar.make(this.layout_mk_plan, "제목을 입력해주세요", Snackbar.LENGTH_LONG).show();
        } else {
            SampleToast.createToast(context, "일정 생성 완료!")?.show()
            ActivityNavigator.with(this).main().start()
            viewModel.additem(MainSchedule(plan_title.text.toString(),viewModel.livePlanColor.value.toString()))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestTodoList()
    }
}





