package com.makeus.milliewillie.ui

import android.content.Context
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.ext.showLongToastSafe
import com.makeus.milliewillie.model.Plan
import com.makeus.milliewillie.ui.fragment.PlanTypeBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.item_home_layout.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class MakePlanActivity :
    BaseDataBindingActivity<ActivityMakePlanBinding>(R.layout.activity_make_plan) {

    private val viewModel by viewModel<MakePlanViewModel>()

    companion object {
        fun getInstance() = MakePlanActivity()
    }

    override fun ActivityMakePlanBinding.onBind() {
        vi = this@MakePlanActivity
        vm = viewModel
        viewModel.bindLifecycle(this@MakePlanActivity)
        // openColorPicker(this@MakePlanActivity)

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
                    })

                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Plan.Todos, ItemPlanTodoBinding>(
                        R.layout.item_plan_todo
                    ) {
                        item = it
                    })

        }
    }

    private fun openColorPicker(context: Context) {
        viewModel.openColorPicker(context) {
            "확인".showLongToastSafe()
            PlanTypeBottomSheetDialogFragment.getInstance()
                .show(supportFragmentManager, "custom_dialog")
        }
    }

    fun onClickDone() {
        onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestPlanTodoList()
    }
}




