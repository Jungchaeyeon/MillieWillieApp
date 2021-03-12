package com.makeus.milliewillie.ui.fragment

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.RoutineExCountBottomSheetBinding
import com.makeus.milliewillie.databinding.RoutineExTimeBottomSheetBinding
import com.makeus.milliewillie.databinding.RoutineExWncBottomSheetBinding

class RoutineOptionTimeBottomSheetFragment
    :BaseDataBindingFragment<RoutineExTimeBottomSheetBinding>(R.layout.routine_ex_time_bottom_sheet){

    override fun RoutineExTimeBottomSheetBinding.onBind() {
        vi = this@RoutineOptionTimeBottomSheetFragment


    }


}