package com.makeus.milliewillie.ui.home.tab2

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutWeightInputBottomSheetBinding
import java.util.*

class WeightRecordBottomSheetFragment: BaseDataBindingBottomSheetFragment<FragmentWorkoutWeightInputBottomSheetBinding>(
    R.layout.fragment_workout_weight_input_bottom_sheet) {

    var goal = ""
    var currnet = ""

    private var clickOk: ((String, String) -> Unit)? = null

    companion object {
        fun getInstance() = WeightRecordBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun FragmentWorkoutWeightInputBottomSheetBinding.onBind() {
        vi = this@WeightRecordBottomSheetFragment
    }


    fun setOnClickOk(clickOk: ((String, String) -> Unit)): WeightRecordBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke(goal, currnet)
        dismiss()
    }

    fun onClickCancel(){
        dismiss()
    }

}