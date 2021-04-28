package com.makeusteam.milliewillie.ui.routine

import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.makeusteam.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.RoutineBottomSheetBinding
import com.makeusteam.milliewillie.util.Log
class ExPartSelectBottomSheetFragment:
    BaseDataBindingBottomSheetFragment<RoutineBottomSheetBinding>(R.layout.routine_bottom_sheet) {

    private var partOfEx : String = ""

    private var clickOk: ((String) -> Unit)? = null

    lateinit var total: AppCompatButton
    lateinit var shoulder: AppCompatButton
    lateinit var chest: AppCompatButton
    lateinit var back: AppCompatButton
    lateinit var abs: AppCompatButton
    lateinit var arm: AppCompatButton
    lateinit var leg: AppCompatButton


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
        arm = binding.rbsBtnArm
        leg = binding.rbsBtnLeg

    }

    fun setTextStatus(position: Int){
        val textList = arrayListOf(total, leg, chest, back, shoulder, arm, abs)

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

        val textList = arrayListOf(total, leg, chest, back, shoulder, arm, abs)

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


//        ExerciseSetBottomSheetFragment.getInstance()
//            .setOnClickOk {
//
//            }.show(fragmentManager!!)
    }
    fun onClickCancel(){
        dismiss()
    }

}