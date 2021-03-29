package com.makeus.milliewillie.ui.home.tab1

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentHomeBinding
import com.makeus.milliewillie.databinding.ItemHomeLayoutBinding
import com.makeus.milliewillie.databinding.ItemMainScheduleBinding
import com.makeus.milliewillie.model.Main
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.MainGetViewModel
import kotlinx.android.synthetic.main.activity_make_plan.view.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.*
import kotlinx.android.synthetic.main.item_plan_todo.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
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
//    lateinit var simpleItemTouchCallback : ItemTouchHelper.SimpleCallback
    companion object {
        fun getInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMain()
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
//                        rvMemoList.addOnItemTouchListener(object  : ItemTouchHelper.SimpleCallback(0,
//                            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT),
//                            RecyclerView.OnItemTouchListener{
//                            override fun onMove(
//                                recyclerView: RecyclerView,
//                                viewHolder: RecyclerView.ViewHolder,
//                                target: RecyclerView.ViewHolder
//                            ): Boolean {
//                                return false
//                            }
//
//                            override fun onSwiped(
//                                viewHolder: RecyclerView.ViewHolder,
//                                direction: Int
//                            ) {
//
//                            }
//
//                            override fun onInterceptTouchEvent(
//                                rv: RecyclerView,
//                                e: MotionEvent
//                            ): Boolean {
//                               return false
//                            }
//
//                            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
//
//                            }
//
//                            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
//
//                            }
//                        })
//                        val simpleItemTouchCallback = object :
//                            ItemTouchHelper.SimpleCallback(
//                                0,
//                                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP
//                            ) {
//                            override fun onMove(
//                                recyclerView: RecyclerView,
//                                viewHolder: RecyclerView.ViewHolder,
//                                target: RecyclerView.ViewHolder
//                            ): Boolean {
//                                Toast.makeText(context, "on Move", Toast.LENGTH_SHORT)
//                                    .show()
//                                return false
//                            }
//
//                            override fun onSwiped(
//                                viewHolder: RecyclerView.ViewHolder,
//                                swipeDir: Int
//                            ) {
//                                Toast.makeText(context, "on Swiped ", Toast.LENGTH_SHORT)
//                                    .show()
//                                //Remove swiped item from list and notify the RecyclerView
//                                val position = viewHolder.adapterPosition
//                                viewModel.planItems.removeAt(position)
//                                viewModel.liveMainPlan.value = viewModel.planItems
//                            }
//                        }
//                        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
//                        itemTouchHelper.attachToRecyclerView(rvMemoList)

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