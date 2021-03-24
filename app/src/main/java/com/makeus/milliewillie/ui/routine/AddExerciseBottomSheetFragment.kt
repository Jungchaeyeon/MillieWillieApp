package com.makeus.milliewillie.ui.routine

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.AddExerciseBottomSheetBinding
import com.makeus.milliewillie.databinding.RoutineExBottomSheetBinding
import com.makeus.milliewillie.ui.home.tab2.WeightAddRecordBottomSheetFragment

class AddExerciseBottomSheetFragment :
    BaseDataBindingBottomSheetFragment<AddExerciseBottomSheetBinding>(R.layout.add_exercise_bottom_sheet) {

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = WeightAddRecordBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun AddExerciseBottomSheetBinding.onBind() {
        vi = this@AddExerciseBottomSheetFragment
    }


    fun setOnClickOk(clickOk: ((String) -> Unit)): AddExerciseBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke(binding.addExEditName.text.toString())
        dismiss()
    }

    fun onClickCancel(){
        dismiss()
    }

}