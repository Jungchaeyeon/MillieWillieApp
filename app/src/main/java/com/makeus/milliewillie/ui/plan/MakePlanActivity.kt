package com.makeus.milliewillie.ui.plan

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.ext.bgTint
import com.makeus.milliewillie.ext.showLongToastSafe
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.model.Plan
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.ui.common.BasicDialogFragment
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.activity_my_page_edit.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.view.*
import kotlinx.android.synthetic.main.item_plan_todo.*
import kotlinx.android.synthetic.main.item_plan_todo.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*


class MakePlanActivity :
    BaseDataBindingActivity<ActivityMakePlanBinding>(R.layout.activity_make_plan) {

    val viewModel by viewModel<MakePlanViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    val context = this

    companion object {
        fun getInstance() = MakePlanActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositoryCached.setValue(LocalKey.PICKDATE, "날짜선택")
        repositoryCached.setValue(LocalKey.ONLYDAY, "")
        repositoryCached.setValue(LocalKey.DAYNIGHT, "")
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


        edtTodo.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (edtTodo.text.toString().isNotEmpty()) {

                    viewModel.addTodo(PlansRequest.Work(edtTodo.text.toString()))
                    //  liveSetImage=R.drawable.emo_9_satisfied
                    edtTodo.text = null
                }
                return@setOnKeyListener true
            }
            false
        }
        viewModel.liveDayAndNight.observe(
            this@MakePlanActivity,
            androidx.lifecycle.Observer { txt_daynight.text = it })
    }

    fun onClickPlanType() {
        //viewModel.liveDate.postValue("")
        repositoryCached.setValue(LocalKey.PICKDATE, "")
        viewModel.liveDayAndNight.postValue("")
        PlanTypeBottomSheetDialogFragment.getInstance()
            .setOnClickDate {
                viewModel.replaceTodo()
                val type = repositoryCached.getPlanType()
                btn_tp.text = type
                when (type) {
                    "휴가", "일정" -> {
                        layout_other_plan.visibility = View.GONE
                        layout_mk_plan.visibility = View.VISIBLE
                        // item_todo.visibility = View.VISIBLE
                        when (type) {
                            "휴가" -> {
                                //  layout_notice_week.visibility = View.GONE
                                layout_leave.visibility = View.VISIBLE
                            }
                            "일정" -> {
                                // layout_notice_week.visibility = View.VISIBLE
                                layout_leave.visibility = View.GONE
                            }
                        }
                    }
                    "외박", "훈련", "면회", "외출", "전투휴무", "당직" -> {
                        layout_other_plan.visibility = View.VISIBLE
                        //layout_notice_week.visibility = View.GONE
                        layout_leave.visibility = View.GONE

                        when (type) {
                            "외박" -> {
                                title_day_and_night.text = "외박"
                                // item_todo.visibility = View.VISIBLE
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
        ActivityNavigator.with(this).plancalendar(viewModel.plansRequest).start()
        txt_daynight.setTextColor(Color.parseColor("#3e3e3e"))
    }

    fun onClickVaca() {
        if (viewModel.liveDayAndNight.value.isNullOrEmpty()) {
            Snackbar.make(this.layout_mk_plan, "휴가일수는 날짜를 선택하신 뒤에 확인 가능합니다.", Snackbar.LENGTH_LONG)
                .show()
        } else {
            ActivityNavigator.with(context).planvacation(viewModel.plansRequest).start()
        }
    }

    fun onClickNotice() {
        if (btn_notice.isChecked) {
            viewModel.plansRequest.push = "T"
            txt_notice.setTextColor(Color.parseColor("#3e3e3e"))
        } else {
            viewModel.plansRequest.push = "F"
            txt_notice.setTextColor(Color.parseColor("#9d9d9d"))
        }
    }

    fun onClickDone() {
        if (plan_title.text.isEmpty()) {
            Snackbar.make(this.layout_mk_plan, "제목을 입력해주세요", Snackbar.LENGTH_LONG).show();
        } else {
            viewModel.plansRequest.title = plan_title.text.toString()
            if (viewModel.planTodos.size != 0) {
                viewModel.plansRequest.work = viewModel.planTodos.toList()
            }
            requestUser()
        }
    }

    fun requestUser() {
        viewModel.requestPlan().subscribe {
            if (it.isSuccess) {
                SampleToast.createToast(context, "일정 생성 완료!")?.show()
                ActivityNavigator.with(this).main().start()
                viewModel.addItem(
                    MainSchedule(
                        plan_title.text.toString(),
                        viewModel.livePlanColor.value.toString()
                    )
                )
            } else {
                "일정 생성 실패".showShortToastSafe()
            }
        }.disposeOnDestroy(this)
    }

    override fun onResume() {
        super.onResume()
        // CalendarActivity에서 선택한 날짜 받아오는 부분
        viewModel.liveDate.value = repositoryCached.getPickDate()
        viewModel.liveOnlyDay.value = repositoryCached.getOnlyDay()
        viewModel.liveDayAndNight.value = repositoryCached.getDayNight()

        val today = Calendar.getInstance().time
        viewModel.plansRequest.startDate = planDateChange(today)
        viewModel.plansRequest.endDate = planDateChange(today)
    }

    fun planDateChange(date: Date): String {
        val planDateFormat = SimpleDateFormat("yyyy-MM-dd")
        Log.e(planDateFormat.format(date).toString(), "날짜로그출력")
        return planDateFormat.format(date).toString()
    }
}





