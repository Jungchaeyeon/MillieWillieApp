package com.makeus.milliewillie.ui.dDay.birthday

import android.view.KeyEvent
import android.widget.Toast
import androidx.lifecycle.Observer
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.MyApplication.Companion.isFocused
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayBirthdayBinding
import com.makeus.milliewillie.databinding.FragmentDDayRecyclerItemBinding
import com.makeus.milliewillie.model.DdayCheckList
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel

class BirthdayFragment: BaseDataBindingFragment<FragmentDDayBirthdayBinding>(R.layout.fragment_d_day_birthday) {

    private val viewModel by viewModel<BirthdayViewModel>()

    override fun FragmentDDayBirthdayBinding.onBind() {
        vi = this@BirthdayFragment
        vm = viewModel
        viewModel.bindLifecycle(this@BirthdayFragment)

        binding.dDayBirthEditTodo.setOnFocusChangeListener { v, hasFocus ->
            isFocused = hasFocus
            Log.e(isFocused.toString())
            if (!hasFocus) {
                Toast.makeText(context, "Focus off", Toast.LENGTH_SHORT).show()
            } else{
                Toast.makeText(context, "Focus on", Toast.LENGTH_SHORT).show()
            }

        }

        dDayBirthTodoRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<DdayCheckList>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<DdayCheckList, FragmentDDayRecyclerItemBinding>(R.layout.fragment_d_day_recycler_item) {
                        vi = this@BirthdayFragment
                        item = it

                    }
                )
        }



        binding.dDayBirthEditTodo.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                Log.e("makeUs", "${binding.dDayBirthEditTodo.text} isChecked")
                if (binding.dDayBirthEditTodo.text.toString().isNotEmpty()) {
                    viewModel.addItem(binding.dDayBirthEditTodo.text.toString())

                    viewModel.checkItemList.observe(this@BirthdayFragment, Observer {
                        viewModel.defaultCheckItemList()
                        Log.e("observe")
                    })
                }
                return@setOnKeyListener true
            }
            false
        }


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