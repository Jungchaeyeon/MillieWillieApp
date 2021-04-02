package com.makeus.milliewillie.ui.home.tab2

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentWorkoutWeightInputBottomSheetBinding
import com.makeus.milliewillie.repository.local.RepositoryCached
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class WeightRecordBottomSheetFragment: BaseDataBindingBottomSheetFragment<FragmentWorkoutWeightInputBottomSheetBinding>(
    R.layout.fragment_workout_weight_input_bottom_sheet) {

    private val viewModel by viewModel<WorkoutViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    private var now = LocalDate.now()
    private var postYear = repositoryCached.getPostYear()
    private var postMonth = repositoryCached.getPostMonth()
    private var postDay = repositoryCached.getPostDay()
    private var currentYear = now.year
    private var currentMonth = now.monthValue
    private var currentDay = now.dayOfMonth

    private var goal: String = ""
    private var current: String = ""

    private var clickOk: ((String, String) -> Unit)? = null

    companion object {
        fun getInstance() = WeightRecordBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun FragmentWorkoutWeightInputBottomSheetBinding.onBind() {
        vi = this@WeightRecordBottomSheetFragment
        vm = viewModel
        dialog?.setCanceledOnTouchOutside(false)
    }

    fun setOnClickOk(clickOk: ((String, String) -> Unit)): WeightRecordBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        goal = binding.wRecordEditGoal.text.toString()
        current = binding.wRecordEditCurrent.text.toString()
        if (current.isBlank()) current = "-1.0"
        if (goal.isBlank()) goal = "-1.0"

//        SampleToast.createToast(context!!,context!!.getString(R.string.toast_weight_record_per_today))?.show()
        clickOk?.invoke(goal, current)
        dismiss()
    }

    fun onClickCancel(){
        current = "-1.0"
        goal = "-1.0"

        clickOk?.invoke(goal, current)
        dismiss()
    }


}