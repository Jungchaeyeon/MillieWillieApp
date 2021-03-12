package com.makeus.milliewillie.ui.fragment

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetBirthBinding
import com.makeus.milliewillie.databinding.DatepickerBottomSheetDDayBinding
import java.util.*

class DatePickerBirthBottomSheetDialogFragment:
    BaseDataBindingBottomSheetFragment<DatepickerBottomSheetBirthBinding>(R.layout.datepicker_bottom_sheet_birth) {

    val liveButton = MutableLiveData<String>()

    private var date : String = ""

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = DatePickerBirthBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun DatepickerBottomSheetBirthBinding.onBind() {
        vi = this@DatePickerBirthBottomSheetDialogFragment
        liveButton.postValue(context?.getString(R.string.ok))

        val today = Calendar.getInstance()
        dpSpinner.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) { view, year, month, day ->
            date = "양력 생일 ${year}년 ${month + 1}월 ${day}일"
        }
    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): DatePickerBirthBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        clickOk?.invoke(date)
        dismiss()
    }
    fun onClickCancel(){
        dismiss()
    }




}