package com.makeusteam.milliewillie.ui.plan

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.*
import com.makeusteam.milliewillie.ext.bgTint
import com.makeusteam.milliewillie.model.PlansRequest
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.SampleToast
import com.makeusteam.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
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
    var array = ArrayList<PlansRequest.PlanVacation>()
    var isPatchPlan = "F"

    companion object {
        fun getInstance() = MakePlanActivity()
    }

    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        isPatchPlan = bundle?.getSerializable(ActivityNavigator.KEY_DATA).toString()
    }


    val requestCode = 1004

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (resultCode == Activity.RESULT_OK) {
                when (requestCode) {
                    1004 -> {
                        array.add(PlansRequest.PlanVacation(data.getStringExtra("vacationId0")
                            ?.toLong(),
                            data.getStringExtra("count0")?.toInt()))
                        array.add(PlansRequest.PlanVacation(data.getStringExtra("vacationId1")
                            ?.toLong(),
                            data.getStringExtra("count1")?.toInt()))
                        array.add(PlansRequest.PlanVacation(data.getStringExtra("vacationId2")
                            ?.toLong(),
                            data.getStringExtra("count2")?.toInt()))
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repositoryCached.setValue(LocalKey.PICKDATE, "????????????")
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

    //    else if(viewModel.liveOnlyDay.value.isNullOrEmpty()){
//        Snackbar.make(this.layout_mk_plan, "?????? ????????? ??????????????????.", Snackbar.LENGTH_SHORT).show();
//    }
    fun onClickDone() {
        if (plan_title.text.isEmpty()) {
            Snackbar.make(this.layout_mk_plan, "????????? ??????????????????", Snackbar.LENGTH_SHORT).show();
        } else if (viewModel.liveDate.value == "????????????" || viewModel.liveDate.value == "") {
            Snackbar.make(this.layout_mk_plan, "????????? ????????? ?????????.", Snackbar.LENGTH_SHORT).show();
        } else if (viewModel.liveDayAndNight.value == "") {
            Snackbar.make(this.layout_mk_plan, "?????? ????????? ???????????? ??????????????????.", Snackbar.LENGTH_SHORT).show()
        } else {
            viewModel.plansRequest.title = plan_title.text.toString()
            viewModel.plansRequest.startDate = repositoryCached.getPlanStartDate()
            viewModel.plansRequest.endDate = repositoryCached.getPlanEndDate()



            if (!viewModel.planTodos.isNullOrEmpty()) {
                viewModel.plansRequest.work = viewModel.planTodos.toList()
            }

            if (viewModel.livePlanType.value.toString() == "??????") {

                viewModel.arrayPlanVac.add(PlansRequest.PlanVacation(repositoryCached.getVac1Id()
                    .toLong(), repositoryCached.getVac1count().toInt()))
                viewModel.arrayPlanVac.add(PlansRequest.PlanVacation(repositoryCached.getVac2Id()
                    .toLong(), repositoryCached.getVac2count().toInt()))
                viewModel.arrayPlanVac.add(PlansRequest.PlanVacation(repositoryCached.getVac3Id()
                    .toLong(), repositoryCached.getVac3count().toInt()))
            }

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                viewModel.plansRequest.pushDeviceToken = task.result

                if(isPatchPlan=="T"){
                    patchPlan()
                }
                else{
                    requestUser()
                }
            }
        }
    }

    fun onClickPlanType() {
        //viewModel.liveDate.postValue("")
        repositoryCached.setValue(LocalKey.PICKDATE, "")
        viewModel.liveDayAndNight.postValue("")
        viewModel.liveOnlyDay.postValue("")
        PlanTypeBottomSheetDialogFragment.getInstance()
            .setOnClickDate {
                viewModel.livePlanType.value = it
                viewModel.replaceTodo()
                Log.e(it, "??????")
                repositoryCached.setValue(LocalKey.PLANTYPE, it.toString())
                val type = repositoryCached.getPlanType()
                btn_tp.text = type
                when (type) {
                    "??????", "??????" -> {
                        layout_other_plan.visibility = View.GONE
                        layout_mk_plan.visibility = View.VISIBLE
                        when (type) {
                            "??????" -> {
                                //  layout_notice_week.visibility = View.GONE
                                layout_leave.visibility = View.VISIBLE
                            }
                            "??????" -> {
                                // layout_notice_week.visibility = View.VISIBLE
                                layout_leave.visibility = View.GONE
                            }
                        }
                    }
                    "??????", "??????", "??????", "??????", "????????????", "??????" -> {
                        layout_other_plan.visibility = View.VISIBLE
                        layout_leave.visibility = View.GONE

                        when (type) {
                            "??????" -> {
                                title_day_and_night.text = "??????"
                                // item_todo.visibility = View.VISIBLE
                            }
                            "??????" -> {
                                title_day_and_night.text = "??????"
                                title_todo.visibility = View.GONE
                            }
                            "??????" -> {
                                title_day_and_night.text = "??????"
                                title_todo.visibility = View.GONE
                            }
                            "??????" -> {
                                title_day_and_night.text = "??????"
                                title_todo.visibility = View.VISIBLE
                            }
                            "????????????" -> {
                                title_day_and_night.text = "????????????"
                                title_todo.visibility = View.GONE
                            }
                            "??????" -> {
                                title_day_and_night.text = "??????"
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

        when (viewModel.livePlanType.value.toString()) {
            "??????", "????????????", "??????", "??????" -> ActivityNavigator.with(this)
                .plancalendaronlyone(viewModel.plansRequest).start()
            else -> ActivityNavigator.with(this).plancalendar(viewModel.plansRequest).start()
        }

        txt_daynight.setTextColor(Color.parseColor("#3e3e3e"))
    }

    fun onClickVaca() {
        if (viewModel.liveDayAndNight.value.isNullOrEmpty()) {
            Snackbar.make(this.layout_mk_plan, "??????????????? ????????? ???????????? ?????? ?????? ???????????????.", Snackbar.LENGTH_LONG)
                .show()
        } else {
            ActivityNavigator.with(context).planvacation().start()
//            ActivityNavigator.with(context).planvacation(viewModel.plansRequest).startForResult(
//                requestCode
//            )
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
        viewModel.requestPlan()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.arrayPlanVac.clear()
                if (it.isSuccess) {
                    //binding.txtMakeplanDone.isEnabled=false
                    SampleToast.createToast(context, "?????? ?????? ??????!")?.show()
                    ActivityNavigator.with(this).main().start()
                    repositoryCached.setValue(LocalKey.PLANID, it.result.planId)
                    Log.e(it.result.planId.toString(), "plan ID")

                } else {
                    Snackbar.make(this.layout_mk_plan, "?????? ?????? ????????? ???????????? ????????????.", Snackbar.LENGTH_LONG)

                }
            }.disposeOnDestroy(this)
    }
    fun patchPlan() {
        viewModel.patchPlan()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                viewModel.arrayPlanVac.clear()
                if (it.isSuccess) {
                    SampleToast.createToast(context, "?????? ?????? ??????!")?.show()
                    ActivityNavigator.with(this).planoutput().start()
                    repositoryCached.setValue(LocalKey.PLANID, it.result.planId)
                    Log.e(it.result.planId.toString(), "plan ID")

                } else {
                    Snackbar.make(this.layout_mk_plan, "?????? ?????? ????????? ???????????? ????????????.", Snackbar.LENGTH_LONG)

                }
            }.disposeOnDestroy(this)
    }

    override fun onResume() {
        super.onResume()
        // CalendarActivity?????? ????????? ?????? ???????????? ??????
        viewModel.liveDate.value = repositoryCached.getPickDate()
        viewModel.liveOnlyDay.value = repositoryCached.getOnlyDay()
        viewModel.liveDayAndNight.value = repositoryCached.getDayNight()

        if (isPatchPlan.equals("T")) {
            layout_plan_type.visibility = View.GONE
            line_plan_type.visibility = View.GONE
        }
    }

    fun planDateChange(date: Date): String {
        val planDateFormat = SimpleDateFormat("yyyy-MM-dd")
        //  Log.e(planDateFormat.format(date).toString(), "??????????????????")
        return planDateFormat.format(date).toString()
    }

    override fun onBackPressed() {
        ActivityNavigator.with(this).main().start()
    }
}





