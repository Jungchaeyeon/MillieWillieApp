package com.makeus.milliewillie.ui.routine

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Switch
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentDDayRecyclerItemBinding
import com.makeus.milliewillie.databinding.RoutineExBottomSheetBinding
import com.makeus.milliewillie.databinding.RoutineExWncRecyclerItemBinding
import com.makeus.milliewillie.model.DdayCheckList
import com.makeus.milliewillie.model.WorkoutSet
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.routine_ex_bottom_sheet.*
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

        binding.rebsWncSwitch.isChecked = true
        Log.e(binding.rebsWncSwitch.isChecked.toString())

        binding.rebsWncEditSetCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 0 ) {
                    if (binding.rebsWncSwitch.isChecked) binding.rebsWncTextTotalSetCount.text = "$s 세트"
                    else binding.rebsWncTextTotalSetCount.text = "1 세트"
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        rebsWncRecyclerSet.run {
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutSet>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutSet, RoutineExWncRecyclerItemBinding>(R.layout.routine_ex_wnc_recycler_item) {
                        vi = this@ExerciseSetBottomSheetFragment
                        item = it
                    }
                )
        }

        binding.rebsWncLayoutAddSet.setOnClickListener {
            viewModel.addItem()
        }

    }

    fun onClickOptionSelect() {
        binding.rebsLayoutOptionSelect.isSelected = !binding.rebsLayoutOptionSelect.isSelected
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

    fun onClickSwitch() {

        if (binding.rebsWncSwitch.isChecked) {
            binding.rebsWncLayoutSetAdd.visibility = View.GONE
        } else {
//            binding.rebsWncEditSetCount.isEnabled = false
//            binding.rebsWncEditSetCount.setText(viewModel.liveDataSetItemListSize.toString())
//            binding.rebsWncTextTotalSetCount.text = "1 세트"

            binding.rebsWncLayoutSetAdd.visibility = View.VISIBLE
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

    override fun onResume() {
        super.onResume()
        viewModel.defaultAddSet()
    }

}