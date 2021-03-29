package com.makeus.milliewillie.ui.home.tab2

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutAddWeightInputBottomSheetBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import java.util.*

class WeightAddRecordBottomSheetFragment: BaseDataBindingBottomSheetFragment<FragmentWorkoutAddWeightInputBottomSheetBinding>(
    R.layout.fragment_workout_add_weight_input_bottom_sheet) {

    private var current = ""

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = WeightAddRecordBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun FragmentWorkoutAddWeightInputBottomSheetBinding.onBind() {
        vi = this@WeightAddRecordBottomSheetFragment


    }


    fun setOnClickOk(clickOk: ((String) -> Unit)): WeightAddRecordBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {

//        if (currnet.isEmpty()){
//            currnet = "0"
//        }
//
//        currnet = binding.recordEditCurrent.text.toString()
//        clickOk?.invoke(currnet)

        current = binding.recordEditCurrent.text.toString()
        if (current.isBlank()) {
            getString(R.string.toast_weight_record).showShortToastSafe()
        } else {
            getString(R.string.toast_weight_record_per_today).showShortToastSafe()
            clickOk?.invoke(current)
            dismiss()
        }

    }

    fun onClickCancel(){
        dismiss()
    }


}