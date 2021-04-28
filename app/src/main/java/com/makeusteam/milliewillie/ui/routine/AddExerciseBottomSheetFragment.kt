package com.makeusteam.milliewillie.ui.routine

import android.os.Bundle
import com.makeusteam.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.AddExerciseBottomSheetBinding

class AddExerciseBottomSheetFragment :
    BaseDataBindingBottomSheetFragment<AddExerciseBottomSheetBinding>(R.layout.add_exercise_bottom_sheet) {

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = AddExerciseBottomSheetFragment()
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