package com.makeus.milliewillie.ui.dDay.birthday

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayBirthdayBinding
import com.makeus.milliewillie.databinding.FragmentDDayRecyclerItemBinding
import com.makeus.milliewillie.model.DdayCheckList
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel

class BirthdayFragment: BaseDataBindingFragment<FragmentDDayBirthdayBinding>(R.layout.fragment_d_day_birthday) {

    private val viewModel by viewModel<BirthdayViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun FragmentDDayBirthdayBinding.onBind() {
        vi = this@BirthdayFragment
        vm = viewModel
//        viewModel.bindLifecycle(this@BirthdayFragment)

        dDayBirthTodoRecycler.run {
//            this.setOnClickListener {
//                Log.e("recycler clicked")
//            }
            this.setOnTouchListener { v, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    Log.e("touch")
                    true
                }
                false
            }
            adapter = BaseDataBindingRecyclerViewAdapter<DdayCheckList>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<DdayCheckList, FragmentDDayRecyclerItemBinding>(R.layout.fragment_d_day_recycler_item) {
                        vi = this@BirthdayFragment
                        item = it
                    }
                )
//
//            BaseDataBindingRecyclerViewAdapter<DdayCheckList>().itemClick = object : BaseDataBindingRecyclerViewAdapter.MyItemClickListener {
//                override fun onItemClick(view: View, position: Int) {
//                    Log.e("item clicked")
//                }
//
//            }

        }

        binding.dDayBirthEditTodo.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (binding.dDayBirthEditTodo.text.toString().isNotEmpty()) {
                    viewModel.addItem(binding.dDayBirthEditTodo.text.toString())
                    binding.dDayBirthEditTodo.setText("")
                    viewModel.checkItemList.observe(this@BirthdayFragment, Observer {
                        viewModel.defaultCheckItemList()
//                        Log.e("observe")
                    })
                }
                return@setOnKeyListener true
            }
            false
        }


    }

    fun checkboxClick() {
        Log.e("touch checkbox")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.checkItemList.removeObservers(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.defaultCheckItemList()
    }

}