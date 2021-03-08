package com.makeus.milliewillie.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetBasicBinding
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import java.util.*


class DatePickerBasicBottomSheetDialogFragment :
    BaseDataBindingBottomSheetFragment<DatepickerBottomSheetBasicBinding>(R.layout.datepicker_bottom_sheet_basic) {


    val liveButton = MutableLiveData<String>()
    val date = MutableLiveData<String>()

    private var clickOk: (() -> Unit)? = null

    companion object {
        fun getInstance() = DatePickerBasicBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun DatepickerBottomSheetBasicBinding.onBind() {
        vi = this@DatePickerBasicBottomSheetDialogFragment
        liveButton.postValue(context?.getString(R.string.ok))

        val today = Calendar.getInstance()
        dpSpinner.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { view, year, month, day ->
            val month = month + 1
            date.postValue(year.toString().plus(".") + month.toString().plus(".") + day.toString())
        }
    }

    fun setOnClickOk(clickOk: (() -> Unit)): DatePickerBasicBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }


    fun onClickOk() {
        clickOk?.invoke()
        dismiss()
    }
    fun onClickCancel(){
        dismiss()
    }

}