package com.makeus.milliewillie.ui.workoutStart

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.AddExerciseBottomSheetBinding
import com.makeus.milliewillie.databinding.WorkoutStartFirstPopUpBinding
import com.makeus.milliewillie.ui.home.tab2.WeightAddRecordBottomSheetFragment
import com.makeus.milliewillie.ui.routine.AddExerciseBottomSheetFragment

class WorkoutStartFirstBottomSheetFragment  :
    BaseDataBindingBottomSheetFragment<WorkoutStartFirstPopUpBinding>(R.layout.workout_start_first_pop_up) {

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = WorkoutStartFirstBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun WorkoutStartFirstPopUpBinding.onBind() {
        vi = this@WorkoutStartFirstBottomSheetFragment
        dialog?.setCanceledOnTouchOutside(false)
    }


    fun setOnClickOk(clickOk: ((String) -> Unit)): WorkoutStartFirstBottomSheetFragment {
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