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

class BirthdayFragment: BaseDataBindingFragment<FragmentDDayBirthdayBinding>(R.layout.fragment_d_day_birthday),
    View.OnClickListener {

    private val viewModel by viewModel<BirthdayViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun FragmentDDayBirthdayBinding.onBind() {
        vm = viewModel

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
                if (binding.dDayBirthEditTodo.text.toString().isNotEmpty()) {

                    viewModel.addItem(DdayCheckList(binding.dDayBirthEditTodo.text.toString()))

                    //래디님 이거 Observer 필요없네요!!
                    //enter누르면 추가하게끔 해놓았어요. Observer필요하시면 정의해주세요!
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

    override fun onClick(v: View?) {
        Log.e(v.toString())
        Log.e("clicked")
    }

}