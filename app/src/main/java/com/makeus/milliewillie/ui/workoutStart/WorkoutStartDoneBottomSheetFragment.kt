package com.makeus.milliewillie.ui.workoutStart

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.WorkoutStartDonePopUpBinding

class WorkoutStartDoneBottomSheetFragment  :
    BaseDataBindingBottomSheetFragment<WorkoutStartDonePopUpBinding>(R.layout.workout_start_done_pop_up) {

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = WorkoutStartDoneBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun WorkoutStartDonePopUpBinding.onBind() {
        vi = this@WorkoutStartDoneBottomSheetFragment
        dialog?.setCanceledOnTouchOutside(false)
    }


    fun setOnClickOk(clickOk: ((String) -> Unit)): WorkoutStartDoneBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke("")
        dismiss()
    }

    fun onClickCancel(){
        dismiss()
    }

}