package com.makeusteam.milliewillie.ui.dDay.certification

import android.view.KeyEvent
import com.makeusteam.base.fragment.BaseDataBindingFragment
import com.makeusteam.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.FragmentDDayCertiRecyclerItemBinding
import com.makeusteam.milliewillie.databinding.FragmentDDayCertificationBinding
import com.makeusteam.milliewillie.model.DdayCheckList
import com.makeusteam.milliewillie.ui.dDay.DatePickerDdayBottomSheetDialogFragment
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