package com.makeusteam.milliewillie.ui.plan

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.DatepickerBottomSheetBasicBinding
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import java.text.SimpleDateFormat
import java.util.*


class DatePickerBasicBottomSheetDialogFragment :
    BaseDataBindingBottomSheetFragment<DatepickerBottomSheetBasicBinding>(R.layout.datepicker_bottom_sheet_basic) {


    val liveButton = MutableLiveData<String>()
    private var date: String = ""
    var reDate: String = ""
    private var clickOk: ((String) -> Unit)? = null
    val today = Calendar.getInstance()

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

        dpSpinner.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)
        ) { view, year, month, day ->

            date = "$year.${month + 1}.$day"
        }
        val cal = Calendar.getInstance()
        val dayOfWeekformat = SimpleDateFormat(" (EE)")
        reDate = dayOfWeekformat.format(cal.time)
    }

    fun setOnClickOk(clickOk: ((String) -> Unit)): DatePickerBasicBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {

        val dateFormat = SimpleDateFormat("yyyy.MM.dd")
        val dateDefault = dateFormat.format(today.time)
       if (date == "") {
            clickOk?.invoke(dateDefault + reDate)
        } else {
             clickOk?.invoke(date + reDate)
        }
        dismiss()
    }

    fun onClickCancel() {
        dismiss()
    }

}