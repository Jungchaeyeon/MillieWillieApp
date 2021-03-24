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
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.RoutineExBottomSheetBinding
import com.makeus.milliewillie.databinding.RoutineExCountRecyclerItemBinding
import com.makeus.milliewillie.databinding.RoutineExTimeRecyclerItemBinding
import com.makeus.milliewillie.databinding.RoutineExWncRecyclerItemBinding
import com.makeus.milliewillie.model.WorkoutSet
import com.makeus.milliewillie.ui.dDay.Classification
import com.makeus.milliewillie.ui.dDay.anniversary.AnniversaryFragment
import com.makeus.milliewillie.ui.dDay.birthday.BirthdayFragment
import com.makeus.milliewillie.ui.dDay.certification.CertificationFragment
import com.makeus.milliewillie.ui.dDay.ncee.NceeFragment
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.routine_ex_bottom_sheet.*
import okhttp3.internal.wait
import org.koin.android.viewmodel.ext.android.viewModel

class ExerciseSetBottomSheetFragment:
    BaseDataBindingFragment<RoutineExBottomSheetBinding>(R.layout.routine_ex_bottom_sheet) {

    enum class SetOptions() {
        WNC,
        COUNT,
        TIME
    }

    companion object {
        fun getInstance() = ExerciseSetBottomSheetFragment()
    }

    val viewModel by viewModel<ExerciseSetViewModel>()

    lateinit var btnWnc: View
    lateinit var btnCount: View
    lateinit var btnTime: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private var clickOk: ((String) -> Unit)? = null

    override fun RoutineExBottomSheetBinding.onBind() {
        vi = this@ExerciseSetBottomSheetFragment
        vm = viewModel
        viewModel.bindLifecycle(this@ExerciseSetBottomSheetFragment)

        btnWnc = binding.rebsBtnWnc
        btnCount = binding.rebsBtnCount
        btnTime = binding.rebsBtnTime

        setBtnStatus(1)
        binding.rebsSwitch.isChecked = true

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

    fun setBtnStatus(position: Int){
        when (position) {
            1 -> {
                setBtnView(btnWnc)
                replaceViewFrame(SetOptions.WNC)
            }
            2 -> {
                setBtnView(btnCount)
                replaceViewFrame(SetOptions.COUNT)
            }
            3 -> {
                setBtnView(btnTime)
                replaceViewFrame(SetOptions.TIME)
            }
        }
    }

    fun setBtnView(btn: View) {
        val btnList = arrayListOf<View>(btnWnc, btnCount, btnTime)

        if (!btn.isSelected) {
            btnList.forEach { view ->
                if (view == btn) btn.isSelected = !btn.isSelected
                else view.isSelected = false
            }
        }
    }

    fun replaceViewFrame(setOptions: SetOptions) {
        when (setOptions) {
            SetOptions.WNC -> {
                binding.rebsWncLayout.visibility = View.VISIBLE
                binding.rebsCountLayout.visibility = View.GONE
                binding.rebsTimeLayout.visibility = View.GONE
            }
            SetOptions.COUNT -> {
                binding.rebsWncLayout.visibility = View.GONE
                binding.rebsCountLayout.visibility = View.VISIBLE
                binding.rebsTimeLayout.visibility = View.GONE
            }
            SetOptions.TIME -> {
                binding.rebsWncLayout.visibility = View.GONE
                binding.rebsCountLayout.visibility = View.GONE
                binding.rebsTimeLayout.visibility = View.VISIBLE
            }

        }

    }

    fun onClickAddItem(view: View) {
        when (view.id) {
            R.id.rebs_btn_plus -> {
                viewModel.increaseSetCount("${viewModel.liveDataSetCount.value!!.toInt() + 1}")
                viewModel.addItem()
            }
            R.id.rebs_btn_minus -> {
                viewModel.decreaseSetCount("${viewModel.liveDataSetCount.value!!.toInt() - 1}")
                viewModel.removeItem()
            }
        }

    }

    fun onClickSwitch() {
        when (binding.rebsSwitch.isChecked) {
            true -> {
                binding.rebsWncLayoutInput.visibility = View.VISIBLE
                binding.rebsCountLayoutInput.visibility = View.VISIBLE
                binding.rebsTimeLayoutInput.visibility = View.VISIBLE

                binding.rebsWncRecyclerSet.visibility = View.GONE
                binding.rebsCountRecyclerSet.visibility = View.GONE
                binding.rebsTimeRecyclerSet.visibility = View.GONE
            }
            false -> {
                binding.rebsWncLayoutInput.visibility = View.GONE
                binding.rebsCountLayoutInput.visibility = View.GONE
                binding.rebsTimeLayoutInput.visibility = View.GONE

                binding.rebsWncRecyclerSet.visibility = View.VISIBLE
                binding.rebsCountRecyclerSet.visibility = View.VISIBLE
                binding.rebsTimeRecyclerSet.visibility = View.VISIBLE
            }
        }



    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): ExerciseSetBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke("")
//        dismiss()
    }
    fun onClickCancel(){
//        dismiss()
    }

    override fun onResume() {
        super.onResume()
        viewModel.defaultAddSet()
    }

}