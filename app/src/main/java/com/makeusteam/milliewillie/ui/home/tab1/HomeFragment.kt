package com.makeusteam.milliewillie.ui.home.tab1

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.makeusteam.base.fragment.BaseDataBindingFragment
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.FragmentHomeBinding
import com.makeusteam.milliewillie.databinding.ItemHomeLayoutBinding
import com.makeusteam.milliewillie.databinding.ItemMainScheduleBinding
import com.makeusteam.milliewillie.model.Main
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.MainGetViewModel
import kotlinx.android.synthetic.main.activity_make_plan.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.*
import kotlinx.android.synthetic.main.item_plan_todo.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class HomeFragment : BaseDataBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    val viewModel by viewModel<MainGetViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    val classImg: Int = 0
    var dDay = ""
    var endDate = ""
    var nowPercentInt = 0
    var nowPercent = ""
    var nowPercentFlt = 0F
    //lateinit var simpleItemTouchCallback : ItemTouchHelper.SimpleCallback

    companion object {
        fun getInstance() = HomeFragment()
    }

    override fun onResume() {
        super.onResume()

        viewModel.getMain(){
            if(!it){
                ActivityNavigator.with(this).login().start()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
    override fun FragmentHomeBinding.onBind() {
        vi = this@HomeFragment
        vm = viewModel

        setClassImg()

        rvMemoList.run {

            adapter = BaseDataBindingRecyclerViewAdapter<Main.Result.PlanMain>()
                .setItemViewType { item, position, isLast ->
                    if (position == 0) 0 else 1
                }
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Main.Result.PlanMain, ItemHomeLayoutBinding>(
                        R.layout.item_home_layout
                    ) {
                        vi = this@HomeFragment
                        vm = viewModel

                        Glide.with(this.profileImg).load(userProfileImgUrl).circleCrop()
                            .placeholder(R.drawable.graphic_profile_big).into(this.profileImg)

                    })
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Main.Result.PlanMain, ItemMainScheduleBinding>(
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

    fun onClickPlanItem(item: Main.Result.PlanMain){
        repositoryCached.setValue(LocalKey.PLANID, item.planId.toString())
        ActivityNavigator.with(this).planoutput().start()
    }

    fun setClassImg() {

    }

//    fun onClickItem() {
//        val nextFrag = HomeFragment()
//        activity?.supportFragmentManager?.beginTransaction()
//            ?.replace(R.id.container, nextFrag, "findThisFragment")
//            ?.addToBackStack(null)?.commit()
//    }


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
    fun onClickCalendar(){
        ActivityNavigator.with(this).maincalendar().start()
    }



}