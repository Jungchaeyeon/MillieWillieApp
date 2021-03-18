package com.makeus.milliewillie.ui.routine

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Switch
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.RoutineExBottomSheetBinding
import com.makeus.milliewillie.databinding.RoutineExCountRecyclerItemBinding
import com.makeus.milliewillie.databinding.RoutineExTimeRecyclerItemBinding
import com.makeus.milliewillie.databinding.RoutineExWncRecyclerItemBinding
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
    }

    private var clickOk: ((String) -> Unit)? = null

    override fun RoutineExBottomSheetBinding.onBind() {
        vi = this@ExerciseSetBottomSheetFragment
        vm = viewModel
        viewModel.bindLifecycle(this@ExerciseSetBottomSheetFragment)

        binding.rebsWncSwitch.isChecked = true
        binding.rebsCountSwitch.isChecked = true
        binding.rebsTimeSwitch.isChecked = true

        Log.e(binding.rebsWncSwitch.isChecked.toString())

        // EditText 이벤트 3종
        binding.rebsWncEditSetCount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 0 ) {
                    if (binding.rebsWncSwitch.isChecked) binding.rebsWncTextTotalSetCount.text = "$s 세트"
                    else binding.rebsWncTextTotalSetCount.text = "1 세트"
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.rebsCountEditSetCount.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 0 ) {
                    if (binding.rebsCountSwitch.isChecked) binding.rebsCountTextTotalSetCount.text = "$s 세트"
                    else binding.rebsCountTextTotalSetCount.text = "1 세트"
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        binding.rebsTimeEditSetCount.addTextChangedListener(object :TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length != 0 ) {
                    if (binding.rebsTimeSwitch.isChecked) binding.rebsTimeTextTotalSetCount.text = "$s 세트"
                    else binding.rebsTimeTextTotalSetCount.text = "1 세트"
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 리사이클러뷰 바인딩 적용 3종
        rebsWncRecyclerSet.run {
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutSet>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutSet, RoutineExWncRecyclerItemBinding>(R.layout.routine_ex_wnc_recycler_item) {
                        vi = this@ExerciseSetBottomSheetFragment
                        item = it
                    }
                )
        }
        rebsCountRecyclerSet.run {
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutSet>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutSet, RoutineExCountRecyclerItemBinding>(R.layout.routine_ex_count_recycler_item) {
                        vi = this@ExerciseSetBottomSheetFragment
                        item = it
                    }
                )
        }
        rebsTimeRecyclerSet.run {
            adapter = BaseDataBindingRecyclerViewAdapter<WorkoutSet>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<WorkoutSet, RoutineExTimeRecyclerItemBinding>(R.layout.routine_ex_time_recycler_item) {
                        vi = this@ExerciseSetBottomSheetFragment
                        item = it
                    }
                )
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

    fun onClickAddItem(view: View) {
        Log.e(view.toString())

        when (view.id) {
            R.id.rebs_wnc_layout_add -> {
                Log.e("clicked add")
                viewModel.addItem(view)
                viewModel.liveDataWncAddSetList.observe(this@ExerciseSetBottomSheetFragment, Observer { viewModel.defaultAddSet() })
                binding.rebsWncEditSetCount.setText("${viewModel.wncSetItemListSize + 1}")
            }
            R.id.rebs_count_layout_add -> {
                Log.e("clicked add2")
                viewModel.addItem(view)
                viewModel.liveDataCountAddSetList.observe(this@ExerciseSetBottomSheetFragment, Observer { viewModel.defaultCountAddSet() })
                binding.rebsCountEditSetCount.setText("${viewModel.countSetItemListSize + 1}")
            }
            R.id.rebs_time_layout_add -> {
                Log.e("clicked add3")
                viewModel.addItem(view)
                viewModel.liveDataTimeAddSetList.observe(this@ExerciseSetBottomSheetFragment, Observer { viewModel.defaultTimeAddSet() })
                binding.rebsTimeEditSetCount.setText("${viewModel.timeSetItemListSize + 1}")
            }

        }
    }

    fun onClickSwitch(view: View) {
        Log.e(view.toString())
        val wncViewList = arrayListOf<View>( binding.rebsWncLayoutAdd, binding.rebsWncRecyclerSet, binding.rebsWncEditSetCount)
        val countViewList = arrayListOf<View>(binding.rebsCountLayoutAdd, binding.rebsCountRecyclerSet, binding.rebsCountEditSetCount)
        val timeViewList = arrayListOf<View>(binding.rebsTimeLayoutAdd, binding.rebsTimeRecyclerSet, binding.rebsTimeEditSetCount)

        val viewList = ArrayList<View>()

        when (view.id) {
            R.id.rebs_wnc_switch -> {
                wncViewList.forEach {
                    viewList.add(it)
                }
            }
            R.id.rebs_count_switch -> {
                countViewList.forEach {
                    viewList.add(it)
                }
            }
            R.id.rebs_time_switch -> {
                timeViewList.forEach {
                    viewList.add(it)
                }
            }

        }

        if ((view as Switch).isChecked) {
            viewList[0].visibility = View.GONE
            viewList[1].visibility = View.GONE
            viewList[2].isEnabled = true
            (viewList[2] as EditText).setText("1")
        } else {
            viewList[2].isEnabled = false
            (viewList[2] as EditText).setText("${viewModel.wncSetItemListSize + 1}")
            binding.rebsWncTextTotalSetCount.text = "1 세트"

            viewList[0].visibility = View.VISIBLE
            viewList[1].visibility = View.VISIBLE
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
        viewModel.defaultCountAddSet()
        viewModel.defaultTimeAddSet()
    }

}