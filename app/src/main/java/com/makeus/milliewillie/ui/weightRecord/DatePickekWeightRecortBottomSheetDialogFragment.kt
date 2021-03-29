package com.makeus.milliewillie.ui.fragment

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetDDayBinding
import com.makeus.milliewillie.databinding.DatepickerBottomSheetWeightRecordBinding
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_weight_record.*
import java.util.*
import kotlin.collections.ArrayList

class DatePickekWeightRecortBottomSheetDialogFragment:
    BaseDataBindingBottomSheetFragment<DatepickerBottomSheetWeightRecordBinding>(R.layout.datepicker_bottom_sheet_weight_record) {

    val liveButton = MutableLiveData<String>()

    private var date : String = ""
    private var dateYear: String = ""
    private var dateMonth: String = ""

    private var clickOk: ((String, String) -> Unit)? = null

    companion object {
        fun getInstance() = DatePickekWeightRecortBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun DatepickerBottomSheetWeightRecordBinding.onBind() {
        vi = this@DatePickekWeightRecortBottomSheetDialogFragment
        liveButton.postValue(context?.getString(R.string.ok))

        val today = Calendar.getInstance()

        val yearList = Array<String>(101) {
            i -> "${i}년"
        }
        val monthList = Array<String>(12) {
                i -> "${i}월"
        }



//        val yearList = ArrayList<String>()
//        val monthList = ArrayList<String>()
        for (i in 0..100) yearList[i] = "${i+1950}년"
        for (i in 0..11) monthList[i] = "${i+1}월"

        yearSpinner.apply {
            minValue = 1950
            maxValue = 2050
            wrapSelectorWheel = false
            displayedValues = yearList

            value = today.get(Calendar.YEAR)
        }

        monthSpinner.apply {
            minValue = 1
            maxValue = 12
            wrapSelectorWheel = false
            displayedValues = monthList
            value = today.get(Calendar.MONTH)+1
        }


    }

    fun setOnClickOk(clickOk: ((String, String) -> Unit)): DatePickekWeightRecortBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
//        if (date.isBlank()) {
//            dateYear = "${Calendar.getInstance().get(Calendar.YEAR)}"
//            dateMonth = "${Calendar.getInstance().get(Calendar.MONTH)+1}"
//        }
        dateYear = yearSpinner.value.toString()
        dateMonth = monthSpinner.value.toString()
        Log.e("dateYear, dateMonth = $dateYear, $dateMonth")
        clickOk?.invoke(dateYear, dateMonth)
        dismiss()
    }
    fun onClickCancel(){
        dismiss()
    }


}
