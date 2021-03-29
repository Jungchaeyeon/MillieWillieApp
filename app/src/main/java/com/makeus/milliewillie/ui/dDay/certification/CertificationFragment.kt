package com.makeus.milliewillie.ui.dDay.certification

import android.view.KeyEvent
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayAnniversaryBinding
import com.makeus.milliewillie.databinding.FragmentDDayCertiRecyclerItemBinding
import com.makeus.milliewillie.databinding.FragmentDDayCertificationBinding
import com.makeus.milliewillie.databinding.FragmentDDayRecyclerItemBinding
import com.makeus.milliewillie.model.DdayCheckList
import com.makeus.milliewillie.ui.fragment.DatePickerBirthBottomSheetDialogFragment
import com.makeus.milliewillie.ui.fragment.DatePickerDdayBottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel

class CertificationFragment: BaseDataBindingFragment<FragmentDDayCertificationBinding>(R.layout.fragment_d_day_certification) {

    private val viewModel by viewModel<CertificationViewModel>()

    override fun FragmentDDayCertificationBinding.onBind() {
        vi = this@CertificationFragment
        vm = viewModel
        viewModel.bindLifecycle(this@CertificationFragment)

        dDayCertiTodoRecycler.run {
            adapter = BaseDataBindingRecyclerViewAdapter<DdayCheckList>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<DdayCheckList, FragmentDDayCertiRecyclerItemBinding>(R.layout.fragment_d_day_certi_recycler_item) {
                        vi = this@CertificationFragment
                        item = it
                    }
                )
        }

        binding.dDayCertiEditTodo.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (binding.dDayCertiEditTodo.text.toString().isNotEmpty()) {
                    viewModel.addItem(DdayCheckList(binding.dDayCertiEditTodo.text.toString()))
                    binding.dDayCertiEditTodo.setText("")
                }
                return@setOnKeyListener true
            }
            false
        }


    }

    fun onClickDdayDate() {
        fragmentManager?.let {
            DatePickerDdayBottomSheetDialogFragment.getInstance()
                .setOnClickOk {date, dotDate, gapDay, year, month ->
                    viewModel.liveDataToday.postValue(date)
                    viewModel.liveDataTodayInfo.postValue(gapDay)
                }.show(it)
        }
    }

    fun setOnClickLocation() {
        ActivityNavigator.with(this).map().start()
    }

}