package com.makeus.milliewillie.ui.fragment

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetDDayBinding
import java.util.*

class DatePickerDdayBottomSheetDialogFragment:
    BaseDataBindingBottomSheetFragment<DatepickerBottomSheetDDayBinding>(R.layout.datepicker_bottom_sheet_d_day) {

    val liveButton = MutableLiveData<String>()

    private var date : String = ""

    private var clickOk: ((String) -> Unit)? = null

    companion object {
        fun getInstance() = DatePickerDdayBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun DatepickerBottomSheetDDayBinding.onBind() {
        vi = this@DatePickerDdayBottomSheetDialogFragment
        liveButton.postValue(context?.getString(R.string.ok))

        val today = Calendar.getInstance()
        dpSpinner.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) { view, year, month, day ->
            date = "${year}년 ${month + 1}월 ${day}일"
        }


    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): DatePickerDdayBottomSheetDialogFragment {
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