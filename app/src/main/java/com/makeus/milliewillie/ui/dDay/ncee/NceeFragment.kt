package com.makeus.milliewillie.ui.dDay.ncee

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayNceeBinding
import com.makeus.milliewillie.ui.fragment.DatePickerDdayBottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel

class NceeFragment: BaseDataBindingFragment<FragmentDDayNceeBinding>(R.layout.fragment_d_day_ncee) {

    private val viewModel by viewModel<NceeViewModel>()


    override fun FragmentDDayNceeBinding.onBind() {
        vi = this@NceeFragment
        vm = viewModel
        viewModel.bindLifecycle(this@NceeFragment)
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

}