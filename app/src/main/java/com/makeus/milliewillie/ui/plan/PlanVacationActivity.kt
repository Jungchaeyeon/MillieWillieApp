package com.makeus.milliewillie.ui.plan

import android.os.Bundle
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.repository.local.RepositoryCached
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


class PlanVacationActivity :
    BaseDataBindingActivity<ActivityPlanVacationBinding>(R.layout.activity_plan_vacation) {

    private val viewModel by viewModel<MakePlanViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    val context = this


    companion object {
        fun getInstance() = PlanVacationActivity()
    }
    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.plansRequest =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as PlansRequest
    }

    override fun ActivityPlanVacationBinding.onBind() {
        vi = this@PlanVacationActivity
        vm= viewModel
        viewModel.bindLifecycle(this@PlanVacationActivity)

    }

    override fun onResume() {
        super.onResume()
        PlanVacationBottomSheetFragment.getInstance()
            .setOnClickOk{
                ActivityNavigator.with(this@PlanVacationActivity).makeplan().start()
            }
            .show(supportFragmentManager)
    }
}





