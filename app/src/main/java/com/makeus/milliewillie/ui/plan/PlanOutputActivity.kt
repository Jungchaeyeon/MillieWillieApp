package com.makeus.milliewillie.ui.plan

import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPlanOutputBinding
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_plan_output.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PlanOutputActivity : BaseDataBindingActivity<ActivityPlanOutputBinding>(R.layout.activity_plan_output) {

    val viewModel by viewModel<PlanOutputViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    override fun ActivityPlanOutputBinding.onBind() {
        vi=this@PlanOutputActivity
        vm = viewModel
        viewModel.bindLifecycle(this@PlanOutputActivity)

        setSupportActionBar(toolbar)
        toolbar.title = " "
    }
    fun onClickDone(){
        viewModel.liveContent.value = edit_plan.text.toString()
        Log.e(edit_plan.text.toString(),"edit 데이터 값 확인")
        viewModel.patchPlanDiary()

    }

    override fun onResume() {
        super.onResume()
        Log.e(repositoryCached.getPlanId(),"id")
        Log.e(34.toLong().toString(),"id")
      //  viewModel.getPlans()
    }

    fun onClickEdit(view: View){
        val popup = PopupMenu(this, view)
        val inflate = popup.menuInflater
        inflate.inflate(R.menu.item_menu_edit, popup.menu)
        popup.setOnMenuItemClickListener(PopupListener())
        popup.show()
    }
    inner class PopupListener: PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item?.itemId) {
                R.id.menu1 -> viewModel.deletePlans()
            }
            return false
        }
    }
}