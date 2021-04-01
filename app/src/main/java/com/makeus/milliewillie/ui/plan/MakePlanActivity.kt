package com.makeus.milliewillie.ui.plan

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.ext.bgTint
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.model.Plans
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.Log.e
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
import kotlin.collections.ArrayList


class MakePlanActivity :
    BaseDataBindingActivity<ActivityMakePlanBinding>(R.layout.activity_make_plan) {

    val viewModel by viewModel<MakePlanViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    val context = this
    var array= ArrayList<PlansRequest.PlanVacation>()

    companion object {
        fun getInstance() = MakePlanActivity()
    }

    val requestCode = 1004

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    1004 -> {
                       array.add(PlansRequest.PlanVacation(data.getStringExtra("vacationId0")?.toLong(),
                           data.getStringExtra("count0")?.toInt()))
                        array.add(PlansRequest.PlanVacation(data.getStringExtra("vacationId1")?.toLong(),
                            data.getStringExtra("count1")?.toInt()))
                        array.add(PlansRequest.PlanVacation(data.getStringExtra("vacationId2")?.toLong(),
                            data.getStringExtra("count2")?.toInt()))
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositoryCached.setValue(LocalKey.PICKDATE, "날짜선택")
        repositoryCached.setValue(LocalKey.ONLYDAY, "")
        repositoryCached.setValue(LocalKey.DAYNIGHT, "")
        val today = Calendar.getInstance().time
        viewModel.plansRequest.startDate = planDateChange(today)
        viewModel.plansRequest.endDate = planDateChange(today)
    }

    override fun ActivityMakePlanBinding.onBind() {
        vi = this@MakePlanActivity
        vm = viewModel
        viewModel.bindLifecycle(this@MakePlanActivity)



        rv_memo_list.isNestedScrollingEnabled = false
        rv_memo_list.run {
            adapter = BaseDataBindingRecyclerViewAdapter<PlansRequest.Work>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<PlansRequest.Work, ItemPlanTodoBinding>(
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
    fun onClickDone() {
        if (plan_title.text.isEmpty()) {
            Snackbar.make(this.layout_mk_plan, "제목을 입력해주세요", Snackbar.LENGTH_LONG).show();
        } else {
            viewModel.plansRequest.title = plan_title.text.toString()
            viewModel.plansRequest.startDate = repositoryCached.getPlanStartDate()
            viewModel.plansRequest.endDate = repositoryCached.getPlanEndDate()

            //viewModel.plansRequest.planVacation = array

            if (viewModel.planTodos.size != 0) {
                viewModel.plansRequest.work = viewModel.planTodos.toList()
            }
            viewModel.plansRequest.planVacation = array

            e(array.toString(), "Make에서 planVac")

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                viewModel.plansRequest.pushDeviceToken = task.result
                requestUser()
            }
        }
    }

    fun onClickPlanType() {
        //viewModel.liveDate.postValue("")
        repositoryCached.setValue(LocalKey.PICKDATE, "")
        viewModel.liveDayAndNight.postValue("")
        PlanTypeBottomSheetDialogFragment.getInstance()
            .setOnClickDate {
                viewModel.livePlanType.value = it
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

//    fun onClickColor() {
//        ColorPickerBottomSheetFragment.getInstance()
//            .setOnClickColor {
//                btn_color.bgTint(it)
//                viewModel.livePlanColor.postValue(it)
//            }
//            .show(supportFragmentManager)
//    }

    fun onClickCalendar() {
        ActivityNavigator.with(this).plancalendar(viewModel.plansRequest).start()
        txt_daynight.setTextColor(Color.parseColor("#3e3e3e"))
    }

    fun onClickVaca() {
        if (viewModel.liveDayAndNight.value.isNullOrEmpty()) {
            Snackbar.make(this.layout_mk_plan, "휴가일수는 날짜를 선택하신 뒤에 확인 가능합니다.", Snackbar.LENGTH_LONG)
                .show()
        } else {
            ActivityNavigator.with(context).planvacation(viewModel.plansRequest).startForResult(
                requestCode
            )
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


    fun requestUser() {
        viewModel.requestPlan().subscribe {
            if (it.isSuccess) {
                SampleToast.createToast(context, "일정 생성 완료!")?.show()
                ActivityNavigator.with(this).main().start()
                repositoryCached.setValue(LocalKey.PLANID, it.result.planId)
                Log.e(it.result.planId.toString(),"plan ID")
//                viewModel.addItem(
//                    MainSchedule(
//                        plan_title.text.toString(),
//                        viewModel.livePlanColor.value.toString()
//                    )
//                )
            } else {
                SampleToast.createToast(this, "일정 생성 실패")?.show()
            }
        }.disposeOnDestroy(this)
    }

    override fun onResume() {
        super.onResume()
        // CalendarActivity에서 선택한 날짜 받아오는 부분
        viewModel.liveDate.value = repositoryCached.getPickDate()
        viewModel.liveOnlyDay.value = repositoryCached.getOnlyDay()
        viewModel.liveDayAndNight.value = repositoryCached.getDayNight()


    }

    fun planDateChange(date: Date): String {
        val planDateFormat = SimpleDateFormat("yyyy-MM-dd")
        //  Log.e(planDateFormat.format(date).toString(), "날짜로그출력")
        return planDateFormat.format(date).toString()
    }
}





