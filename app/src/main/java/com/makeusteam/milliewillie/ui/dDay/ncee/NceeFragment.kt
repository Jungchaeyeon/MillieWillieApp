package com.makeusteam.milliewillie.ui.dDay.ncee

import android.view.KeyEvent
import com.makeusteam.base.fragment.BaseDataBindingFragment
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.FragmentDDayNceeBinding
import com.makeusteam.milliewillie.databinding.FragmentDDayNceeRecyclerItemBinding
import com.makeusteam.milliewillie.model.DdayCheckList
import com.makeusteam.milliewillie.ui.dDay.DatePickerDdayBottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel

class NceeFragment: BaseDataBindingFragment<FragmentDDayNceeBinding>(R.layout.fragment_d_day_ncee) {

    private val viewModel by viewModel<NceeViewModel>()


    override fun FragmentDDayNceeBinding.onBind() {
        vi = this@NceeFragment
        vm = viewModel
        viewModel.bindLifecycle(this@NceeFragment)

        dDayNceeTodoRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<DdayCheckList>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<DdayCheckList, FragmentDDayNceeRecyclerItemBinding>(R.layout.fragment_d_day_ncee_recycler_item) {
                        vi = this@NceeFragment
                        item = it
                    }
                )
        }

        binding.dDayNceeEditTodo.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (binding.dDayNceeEditTodo.text.toString().isNotEmpty()) {
                    viewModel.addItem(DdayCheckList(binding.dDayNceeEditTodo.text.toString()))
                    binding.dDayNceeEditTodo.setText("")
                }
                return@setOnKeyListener true
            }
            false
        }

    }

    fun onClickDdayDate() {
        fragmentManager?.let {
            DatePickerDdayBottomSheetDialogFragment.getInstance()
                .setOnClickOk {date, gapDay ->
                    viewModel.liveDataToday.postValue(date)
                    viewModel.liveDataTodayInfo.postValue(gapDay)
                }.show(it)
        }
    }

    fun setOnClickLocation() {
        ActivityNavigator.with(this).map().start()
    }

}