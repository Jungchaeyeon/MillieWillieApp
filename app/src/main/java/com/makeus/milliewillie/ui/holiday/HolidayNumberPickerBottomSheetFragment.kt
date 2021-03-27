package com.makeus.milliewillie.ui.holiday

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetBasicBinding
import com.makeus.milliewillie.databinding.NumberpickerBottomSheetHoliBinding
import com.makeus.milliewillie.ui.intro.UserViewModel
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.numberpicker_bottom_sheet_holi.*
import org.koin.android.viewmodel.compat.SharedViewModelCompat
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.text.SimpleDateFormat
import java.util.*


class HolidayNumberPickerBottomSheetFragment :
    BaseDataBindingBottomSheetFragment<NumberpickerBottomSheetHoliBinding>(R.layout.numberpicker_bottom_sheet_holi) {


    val liveButton = MutableLiveData<String>()
    val viewModel : HoliViewModel by sharedViewModel()
    private var clickOk: ((String) -> Unit)? = null
    val today = Calendar.getInstance()

    companion object {
        fun getInstance() = HolidayNumberPickerBottomSheetFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun NumberpickerBottomSheetHoliBinding.onBind() {
        vi = this@HolidayNumberPickerBottomSheetFragment
        liveButton.postValue(context?.getString(R.string.ok))

        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
        npSpinner.minValue=0
        npSpinner.maxValue = viewModel.pickableMax
        npSpinner.value=0
    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): HolidayNumberPickerBottomSheetFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {

        clickOk?.invoke(np_spinner.value.toString())
        dismiss()
    }

    fun onClickCancel() {
        dismiss()
    }

    override fun onResume() {
        super.onResume()
    }

}