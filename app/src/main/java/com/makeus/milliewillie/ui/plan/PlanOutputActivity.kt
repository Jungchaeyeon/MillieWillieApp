package com.makeus.milliewillie.ui.plan

import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityPlanOutputBinding
import com.makeus.milliewillie.databinding.ItemOutputPlanTodoBinding
import com.makeus.milliewillie.databinding.ItemPlanMemoBinding
import com.makeus.milliewillie.databinding.ItemPlanTodoBinding
import com.makeus.milliewillie.model.PlansGet
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.activity_plan_output.*
import kotlinx.android.synthetic.main.item_plan_memo.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PlanOutputActivity : BaseDataBindingActivity<ActivityPlanOutputBinding>(R.layout.activity_plan_output) {

    val viewModel by viewModel<PlanOutputViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    override fun ActivityPlanOutputBinding.onBind() {
        vi = this@PlanOutputActivity
        vm = viewModel
        viewModel.bindLifecycle(this@PlanOutputActivity)
        setSupportActionBar(toolbar)
        toolbar.title = " "

        binding.rvTodo.run {
            adapter = BaseDataBindingRecyclerViewAdapter<PlansGet.Result.Work>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<PlansGet.Result.Work, ItemOutputPlanTodoBinding>(
                        R.layout.item_output_plan_todo
                    ) {
                        vi = this@PlanOutputActivity
                        item = it
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
    fun onClickDone(item : PlansGet.Result.Diary){
//        viewModel.liveContent.value = edit_plan.text.toString()
//        Log.e(edit_plan.text.toString(),"edit 데이터 값 확인")
        repositoryCached.setValue(LocalKey.DIARYID,item.diaryId)
        viewModel.liveContent.value = edit_plan.text.toString()
        viewModel.patchPlanDiary()
        SampleToast.createToast(this,"")?.show()
    }

    override fun onResume() {
        super.onResume()
        Log.e(repositoryCached.getPlanId(),"id")
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