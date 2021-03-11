package com.makeus.milliewillie.ui.bottom_navi_fragment

import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentHomeBinding
import com.makeus.milliewillie.databinding.ItemHomeLayoutBinding
import com.makeus.milliewillie.databinding.ItemMainScheduleBinding
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.ui.MainViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class HomeFragment :
    BaseDataBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    val viewModel by viewModel<MainViewModel>()
    lateinit var simpleCallback: ItemTouchHelper.SimpleCallback

    companion object {
        fun getInstance() = HomeFragment()
    }

    override fun FragmentHomeBinding.onBind() {
        vi = this@HomeFragment
        vm = viewModel
        viewModel.bindLifecycle(requireActivity())


        rvMemoList.run {
            adapter = BaseDataBindingRecyclerViewAdapter<MainSchedule>()
                .setItemViewType { item, position, isLast ->
                    if (position == 0) 0 else 1
                }
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MainSchedule, ItemHomeLayoutBinding>(
                        R.layout.item_home_layout
                    ) {
                        vm = viewModel

                    })

                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MainSchedule, ItemMainScheduleBinding>(
                        R.layout.item_main_schedule
                    ) {
                        vm = viewModel
                        if (viewModel.liveMainScheduleList.value?.isNotEmpty() == true) {
                            txt_blank.visibility = View.GONE
                        }
//                                rvRecommendKeyword.run {
//                                    adapter = BaseDataBindingRecyclerViewAdapter<MainSchedule>()
//                                            .addViewType(
//                                                    BaseDataBindingRecyclerViewAdapter.MultiViewType<Spot, ItemRecommendKeywordBinding>(R.layout.item_recommend_keyword) {
//                                                        item = it
//                                                    })
                        item = it
                    })

        }
    }


    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, nextFrag, "findThisFragment")
            ?.addToBackStack(null)?.commit()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestMainScheduleData()
    }
}