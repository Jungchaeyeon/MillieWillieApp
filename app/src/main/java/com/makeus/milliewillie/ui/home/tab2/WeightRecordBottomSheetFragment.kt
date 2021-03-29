package com.makeus.milliewillie.ui.home.tab2

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutWeightInputBottomSheetBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.util.Log
import java.util.*

class WeightRecordBottomSheetFragment: BaseDataBindingBottomSheetFragment<FragmentWorkoutWeightInputBottomSheetBinding>(
    R.layout.fragment_workout_weight_input_bottom_sheet) {

    private var goal: String = ""
    private var current: String = ""

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
        goal = binding.wRecordEditGoal.text.toString()
        current = binding.wRecordEditCurrent.text.toString()
        if (current.isBlank() || goal.isBlank()) {
            getString(R.string.toast_weight_record).showShortToastSafe()
        } else {
            getString(R.string.toast_weight_record_per_today).showShortToastSafe()
            clickOk?.invoke(goal, current)
            dismiss()
        }
    }

    fun onClickCancel(){
        dismiss()
    }

}