package com.makeusteam.milliewillie.ui.plan

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityPlanOutputBinding
import com.makeusteam.milliewillie.databinding.ItemOutputPlanTodoBinding
import com.makeusteam.milliewillie.databinding.ItemPlanMemoBinding
import com.makeusteam.milliewillie.model.PlansGet
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.SampleToast
import com.makeusteam.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.activity_plan_output.*
import kotlinx.android.synthetic.main.item_output_plan_todo.*
import kotlinx.android.synthetic.main.item_output_plan_todo.view.*
import kotlinx.android.synthetic.main.item_plan_memo.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class PlanOutputActivity :
    BaseDataBindingActivity<ActivityPlanOutputBinding>(R.layout.activity_plan_output) {

    val viewModel by viewModel<PlanOutputViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun ActivityPlanOutputBinding.onBind() {
        vi = this@PlanOutputActivity
        vm = viewModel
        viewModel.bindLifecycle(this@PlanOutputActivity)
        //setSupportActionBar(toolbar)
        //toolbar.title = " "
        binding
        binding.rvTodo.run {
            adapter = BaseDataBindingRecyclerViewAdapter<PlansGet.Result.Work>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<PlansGet.Result.Work, ItemOutputPlanTodoBinding>(
                        R.layout.item_output_plan_todo
                    ) {
                        vi = this@PlanOutputActivity
                        item = it
                        if(it.processingStatus=="T"){
                            this.textTodo.isActivated= !isActivated
                            this.textTodo.setTextColor(Color.parseColor("#9d9d9d"))
                            this.textTodo.background=getDrawable(R.drawable.layout_middleline)
                        }

                    })
        }
        binding.rvMemo.run {
            adapter = BaseDataBindingRecyclerViewAdapter<PlansGet.Result.Diary>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<PlansGet.Result.Diary, ItemPlanMemoBinding>(
                        R.layout.item_plan_memo
                    ) {
                        vi = this@PlanOutputActivity
                        item = it

                    })
        }
    }

    fun onClickDone(item: PlansGet.Result.Diary) {
//        viewModel.liveContent.value = edit_plan.text.toString()
//        Log.e(edit_plan.text.toString(),"edit 데이터 값 확인")
        repositoryCached.setValue(LocalKey.DIARYID, item.diaryId)
        viewModel.liveContent.value = item.content
        viewModel.patchPlanDiary()
        SampleToast.createToast(this, "일정 저장 완료")?.show()
    }

    @SuppressLint("ClickableViewAccessibility")
    fun onClickChkBox(view: View, item: PlansGet.Result.Work) {
        Log.e(item.processingStatus, "preocesstiongStatus")
        repositoryCached.setValue(LocalKey.WORKID, item.workId)
        view.isActivated = !view.isActivated
        if (view.isActivated){
            view.text_todo.setTextColor(Color.parseColor("#9d9d9d"))
            view.text_todo.background= getDrawable(R.drawable.layout_middleline)
        }
        else {view.text_todo.setTextColor(Color.parseColor("#3e3e3e"))
            view.text_todo.background= getDrawable(R.drawable.background_transparent)
        }
        viewModel.patchDiary()
    }

    override fun onResume() {
        super.onResume()
        Log.e(repositoryCached.getPlanId(), "id")
        viewModel.getPlans() {
            if (it) {
                Log.e(viewModel.planType.value.toString(), "훈련인지")
                if (viewModel.planType.value.toString() == "훈련") {
                    binding.cautionTrainOutput.visibility = View.VISIBLE
                }

            }
        }
    }

    fun onClickEdit(view: View) {
        val popup = PopupMenu(this, view)
        val inflate = popup.menuInflater
        inflate.inflate(R.menu.item_menu_edit, popup.menu)
        popup.setOnMenuItemClickListener(PopupListener())
        popup.show()
    }

    fun onClickBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        ActivityNavigator.with(this).main().start()
    }

    inner class PopupListener : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu1 -> {
                    ActivityNavigator.with(this@PlanOutputActivity).makeplanwith("T").start()
                    return true
                }
                R.id.menu2 -> {
                    viewModel.deletePlans()
                }
            }
            ActivityNavigator.with(this@PlanOutputActivity).main().start()
            return false
        }
    }
}