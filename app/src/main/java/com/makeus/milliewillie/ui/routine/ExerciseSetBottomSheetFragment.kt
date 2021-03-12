package com.makeus.milliewillie.ui.routine

import android.os.Bundle
import android.view.View
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.RoutineExBottomSheetBinding
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.ext.android.viewModel

class ExerciseSetBottomSheetFragment:
    BaseDataBindingBottomSheetFragment<RoutineExBottomSheetBinding>(R.layout.routine_ex_bottom_sheet) {

    companion object {
        fun getInstance() = ExerciseSetBottomSheetFragment()
    }

    val viewModel by viewModel<ExerciseSetViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
//        setContentView<RoutineExBottomSheetBinding>(activity!!, R.layout.routine_ex_bottom_sheet)
    }

    private var clickOk: ((String) -> Unit)? = null

    override fun RoutineExBottomSheetBinding.onBind() {
        vi = this@ExerciseSetBottomSheetFragment
        vm = viewModel
        viewModel.bindLifecycle(this@ExerciseSetBottomSheetFragment)


    }

    fun onClickOptionSelect() {
        binding.rebsLayoutOptionSelect.isSelected = !binding.rebsLayoutOptionSelect.isSelected
        Log.e(binding.rebsLayoutOptionSelect.isSelected.toString())
        when (binding.rebsLayoutOptionSelect.isSelected) {
            true -> {
                binding.rebsLayoutOptions.visibility = View.VISIBLE
                binding.rebsWncLayout.visibility = View.GONE
                binding.rebsCountLayout.visibility = View.GONE
                binding.rebsTimeLayout.visibility = View.GONE
            }
            false -> {
                binding.rebsLayoutOptions.visibility = View.INVISIBLE
            }
        }
    }

    fun onClickOption(position: Int) {
        binding.rebsLayoutOptionSelect.isSelected = !binding.rebsLayoutOptionSelect.isSelected
        when (position) {
            1 -> {
                viewModel.liveDataRoutineKind.postValue(binding.rebsTextFirst.text.toString())
                binding.rebsLayoutOptions.visibility = View.GONE
                binding.rebsWncLayout.visibility = View.VISIBLE
            }
            2 -> {
                viewModel.liveDataRoutineKind.postValue(binding.rebsTextSecond.text.toString())
                binding.rebsLayoutOptions.visibility = View.GONE
                binding.rebsCountLayout.visibility = View.VISIBLE
            }
            3 -> {
                viewModel.liveDataRoutineKind.postValue(binding.rebsTextThird.text.toString())
                binding.rebsLayoutOptions.visibility = View.GONE
                binding.rebsTimeLayout.visibility = View.VISIBLE
            }
        }
    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): ExerciseSetBottomSheetFragment {
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