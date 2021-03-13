package com.makeus.milliewillie.ui.plan

import android.os.Bundle
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ColorPickerBottomSheetBinding
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.item_plan_layout.*
import kotlinx.android.synthetic.main.item_plan_type.*
import kotlinx.android.synthetic.main.plan_type_bottom_sheet.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class ColorPickerBottomSheetFragment :
    BaseDataBindingBottomSheetFragment<ColorPickerBottomSheetBinding>(R.layout.color_picker_bottom_sheet) {

    val viewModel by viewModel<MakePlanViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    private var clickColor: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = ColorPickerBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun ColorPickerBottomSheetBinding.onBind() {
        vi = this@ColorPickerBottomSheetFragment
        vm = viewModel


    }
//    fun onClickColorPick(color: String){
//        repositoryCached.setValue(LocalKey.COLOR, Color.parseColor(color))
//    }

    fun setOnClickColor(clickColor: ((String) -> Unit)): ColorPickerBottomSheetFragment {
        this.clickColor = clickColor
        return this
    }


    fun onClickColor(color: String) {
        clickColor?.invoke(color)
        dismiss()
    }


    override fun onResume() {
        super.onResume()
        viewModel.requestPlanTypeList()
    }

}