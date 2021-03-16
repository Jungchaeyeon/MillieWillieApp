package com.makeus.milliewillie.ui.fragment

import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.RoutineExWncBottomSheetBinding
import com.makeus.milliewillie.util.Log

class RoutineOptionWeightNCountBottomSheetFragment
    :BaseDataBindingFragment<RoutineExWncBottomSheetBinding>(R.layout.routine_ex_wnc_bottom_sheet){


    override fun RoutineExWncBottomSheetBinding.onBind() {
        vi = this@RoutineOptionWeightNCountBottomSheetFragment

        Log.e("RoutineOptionWeightNCountBottomSheetFragment called")

    }


}