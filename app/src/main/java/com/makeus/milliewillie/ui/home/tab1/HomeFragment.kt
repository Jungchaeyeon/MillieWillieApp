package com.makeus.milliewillie.ui.home.tab1

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.view.View
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentHomeBinding
import com.makeus.milliewillie.databinding.ItemHomeLayoutBinding
import com.makeus.milliewillie.databinding.ItemMainScheduleBinding
import com.makeus.milliewillie.model.MainSchedule
import com.makeus.milliewillie.ui.MainViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.*
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class HomeFragment :
    BaseDataBindingFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    val viewModel by viewModel<MainViewModel>()
    lateinit var simpleCallback: ItemTouchHelper.SimpleCallback
    var calFlag = false

    companion object {
        fun getInstance() = HomeFragment()
    }

    override fun FragmentHomeBinding.onBind() {
        vi = this@HomeFragment
        vm = viewModel
        //  viewModel.bindLifecycle(requireActivity())

        rvMemoList.run {
            // ItemTouchHelper(simpleItemTouchCallback).attachToRecyclerView(this)

            adapter = BaseDataBindingRecyclerViewAdapter<MainSchedule>()
                .setItemViewType { item, position, isLast ->
                    if (position == 0) 0 else 1
                }
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MainSchedule, ItemHomeLayoutBinding>(
                        R.layout.item_home_layout
                    ) {
                        vi = this@HomeFragment
                    })

                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<MainSchedule, ItemMainScheduleBinding>(
                        R.layout.item_main_schedule
                    ) {

                        if (viewModel.liveMainScheduleList.value?.isEmpty() == true) {
                            txt_blank.visibility = View.VISIBLE
                        }
                        item = it
                    })

        }
    }

    private val simpleItemTouchCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition


                binding.rvMemoList.adapter?.notifyItemRemoved(position)
            }
        }

    class CurrentDayDecorator(context: Activity?, currentDay: CalendarDay) : DayViewDecorator {
        private val drawable: Drawable?
        var myDay = currentDay
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == myDay
        }

        override fun decorate(view: DayViewFacade) {
            view.setSelectionDrawable(drawable!!)
        }

        init {
            // You can set background for Decorator via drawable here
            drawable = ContextCompat.getDrawable(context!!, R.drawable.indicator_dot_on)
        }
    }



    fun onClickItem() {
        val nextFrag = HomeFragment()
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.container, nextFrag, "findThisFragment")
            ?.addToBackStack(null)?.commit()
    }

    fun onClickCalendar() {
        if (!calFlag) {
            calendar_view.visibility = View.VISIBLE
            calFlag = true
         //   val mydate = CalendarDay.from(2021, 3,  15) // year, month, date
         //   calendar_view.addDecorators(CurrentDayDecorator(requireActivity(), mydate))
        } else {
            calendar_view.visibility = View.GONE
            calFlag = false
        }
    }

    fun onClickMyPage() {
        ActivityNavigator.with(this).mypage().start()
    }

    fun onClickEdit() {
        ActivityNavigator.with(this).mypageedit().start()
    }

    override fun onResume() {
        super.onResume()
        viewModel.requestMainScheduleData()
    }
}