package com.makeus.milliewillie.ui.routine

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.RoutineBottomSheetBinding
import com.makeus.milliewillie.util.Log
class ExPartSelectBottomSheetFragment:
    BaseDataBindingBottomSheetFragment<RoutineBottomSheetBinding>(R.layout.routine_bottom_sheet) {

    private var partOfEx : String = ""

    private var clickOk: ((String) -> Unit)? = null

    lateinit var total: AppCompatButton
    lateinit var shoulder: AppCompatButton
    lateinit var chest: AppCompatButton
    lateinit var back: AppCompatButton
    lateinit var abs: AppCompatButton
    lateinit var triceps: AppCompatButton
    lateinit var biceps: AppCompatButton
    lateinit var forearm: AppCompatButton
    lateinit var arm: AppCompatButton
    lateinit var hip: AppCompatButton
    lateinit var thigh: AppCompatButton
    lateinit var calf: AppCompatButton
    lateinit var wholeBody: AppCompatButton
    lateinit var core: AppCompatButton


    companion object {
        fun getInstance() = ExPartSelectBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun RoutineBottomSheetBinding.onBind() {
        vi = this@ExPartSelectBottomSheetFragment

        total = binding.rbsBtnTotal
        shoulder = binding.rbsBtnShoulder
        chest = binding.rbsBtnChest
        back = binding.rbsBtnBack
        abs = binding.rbsBtnAbs
        triceps = binding.rbsBtnTriceps
        biceps = binding.rbsBtnBiceps
        forearm = binding.rbsBtnForearm
        arm = binding.rbsBtnArm
        hip = binding.rbsBtnHip
        thigh = binding.rbsBtnThigh
        calf = binding.rbsBtnCalf
        wholeBody = binding.rbsBtnWholeBody
        core = binding.rbsBtnCore
    }

    fun setTextStatus(position: Int){
        val textList = arrayListOf(total, shoulder, chest, back, abs, triceps, biceps,
            forearm, arm, hip, thigh, calf, wholeBody, core)

        if (!textList[position-1].isSelected) {
            textList.forEach { view ->
                if (textList[position-1] == view) {
                    view.isSelected = true
                    partOfEx = view.text.toString()
                    Log.e(partOfEx)
                }
                else view.isSelected = false
            }
        }

        onClickOk()

    }

    fun setBtnView(btn: AppCompatButton) {

        val textList = arrayListOf(total, shoulder, chest, back, abs, triceps, biceps,
            forearm, arm, hip, thigh, calf, wholeBody, core)

        if (!btn.isSelected) {
            textList.forEach { view ->
                if (btn == view) {
                    view.isSelected = true
                    partOfEx = view.text.toString()
                    Log.e(partOfEx)
                }
                else view.isSelected = false
            }
        }
    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): ExPartSelectBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke(partOfEx)
        dismiss()

        ExerciseSetBottomSheetFragment.getInstance()
            .setOnClickOk {}.show(fragmentManager!!)
    }
    fun onClickCancel(){
        dismiss()
    }

}