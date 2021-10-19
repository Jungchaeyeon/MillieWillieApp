package com.makeusteam.milliewillie.ui.home.tab1

import android.view.View
import com.makeusteam.base.fragment.BaseDataBindingFragment
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.FragmentHomeBinding
import com.makeusteam.milliewillie.databinding.ItemHomeLayoutBinding
import com.makeusteam.milliewillie.databinding.ItemMainScheduleBinding
import com.makeusteam.milliewillie.model.main.PlanMain
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_make_plan.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.*
import kotlinx.android.synthetic.main.item_plan_todo.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class HomeFragment : BaseDataBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    val viewModel by viewModel<HomeViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    override fun onResume() {
        super.onResume()

        viewModel.getHomeInfo{notRegistered->
            if(!notRegistered){
                ActivityNavigator.with(this).login().start()
            }
        }
    }

    override fun FragmentHomeBinding.onBind() {
        vi = this@HomeFragment
        vm = viewModel


            rvMemoList.run {

                adapter = BaseDataBindingRecyclerViewAdapter<PlanMain>()
                    .setItemViewType { item, position, isLast ->
                        if (position == 0) 0 else 1
                    }
                    .addViewType(
                        BaseDataBindingRecyclerViewAdapter.MultiViewType<PlanMain, ItemHomeLayoutBinding>(
                            R.layout.item_home_layout
                        ) {
                            vi = this@HomeFragment
                            vm = viewModel


                        })
                    .addViewType(
                        BaseDataBindingRecyclerViewAdapter.MultiViewType<PlanMain, ItemMainScheduleBinding>(
                            R.layout.item_main_schedule
                        ) {
                            vi = this@HomeFragment
                            if (this@HomeFragment.viewModel.planItems.size >= 2) {
                                txt_blank.visibility = View.GONE
                            }
                            item = it


                        })
            }
    }

    fun onClickPlanItem(item: PlanMain){
        repositoryCached.setValue(LocalKey.PLANID, item.planId.toString())
        ActivityNavigator.with(this).planoutput().start()
    }

    fun onClickEdit() {
        ActivityNavigator.with(this).infomili().start()
    }

    fun onClickHoli() {
        ActivityNavigator.with(this).holiday().start()
    }
    fun onClickCalendar(){
        ActivityNavigator.with(this).maincalendar().start()
    }

    companion object {
        fun getInstance() = HomeFragment()
    }

}