package com.makeus.milliewillie.ui.dDay.birthday

import android.annotation.SuppressLint
import android.view.KeyEvent
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayBirthRecyclerItemBinding
import com.makeus.milliewillie.databinding.FragmentDDayBirthdayBinding
import com.makeus.milliewillie.model.DdayCheckList
import org.koin.android.viewmodel.ext.android.viewModel

class BirthdayFragment: BaseDataBindingFragment<FragmentDDayBirthdayBinding>(R.layout.fragment_d_day_birthday) {

    private val viewModel by viewModel<BirthdayViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun FragmentDDayBirthdayBinding.onBind() {
        vm = viewModel

        dDayBirthTodoRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<DdayCheckList>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<DdayCheckList, FragmentDDayBirthRecyclerItemBinding>(R.layout.fragment_d_day_birth_recycler_item) {
                        vi = this@BirthdayFragment
                        item = it
                    }
                )
        }

        binding.dDayBirthEditTodo.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (binding.dDayBirthEditTodo.text.toString().isNotEmpty()) {
                    viewModel.addItem(DdayCheckList(binding.dDayBirthEditTodo.text.toString()))
                    binding.dDayBirthEditTodo.setText("")

                }
                return@setOnKeyListener true
            }
            false
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
//        viewModel.checkItemList.removeObservers(this)
    }

    override fun onResume() {
        super.onResume()
        viewModel.defaultCheckItemList()
    }

}