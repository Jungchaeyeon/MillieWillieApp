package com.makeus.milliewillie.ui.home.tab1

import android.view.View
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentHomeBinding
import com.makeus.milliewillie.databinding.ItemHomeLayoutBinding
import com.makeus.milliewillie.databinding.ItemMainScheduleBinding
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.MainGetViewModel
import com.makeus.milliewillie.ui.MainViewModel
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_make_plan.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.*
import kotlinx.android.synthetic.main.item_plan_todo.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class HomeFragment : BaseDataBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    val viewModel by sharedViewModel<MainGetViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    val classImg: Int = 0
    var dDay = ""
    var endDate = ""
    var nowPercentInt = 0
    var nowPercent = ""
    var nowPercentFlt = 0F

    companion object {
        fun getInstance() = HomeFragment()
    }

    override fun FragmentHomeBinding.onBind() {
        vi = this@HomeFragment
        vm = viewModel

        setClassImg()

        rvMemoList.run {

            adapter = BaseDataBindingRecyclerViewAdapter<MainSchedule>()
                .setItemViewType { item, position, isLast ->
                    if (position == 0) 0 else 1
                }
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MainSchedule, ItemHomeLayoutBinding>(
                        R.layout.item_home_layout
                    ) {
                        vi = this@HomeFragment
                        vm = viewModel
                    })
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MainSchedule, ItemMainScheduleBinding>(
                        R.layout.item_main_schedule
                    ) {
                        if (this@HomeFragment.viewModel.planItems.size >= 2) {
                            txt_blank.visibility = View.GONE
                        }
                        item = it
                    })

        }
    }


    fun setClassImg() {

    }

    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, nextFrag, "findThisFragment")
            ?.addToBackStack(null)?.commit()
    }

    fun onClickCalendar() {
        ActivityNavigator.with(this).planoutput().start()
    }

    //x
    fun onClickMyPage() {
        ActivityNavigator.with(this).mypage().start()
    }

    fun onClickEdit() {
        ActivityNavigator.with(this).infomili().start()
    }

    fun endDate(): String {
        endDate = repositoryCached.getEnd()
        return endDate.substring(2)
    }

    fun onClickHoli() {
        ActivityNavigator.with(this).holiday().start()
    }

}