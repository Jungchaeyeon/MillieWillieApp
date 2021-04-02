package com.makeus.milliewillie.ui.fragment

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetProfileBirthdayBinding
import com.makeus.milliewillie.databinding.DatepickerBottomSheetWeightRecordBinding
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_profile_birthday.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_weight_record.*
import java.util.*

class DatePickekProfileBirthDayBottomSheetDialogFragment:
    BaseDataBindingBottomSheetFragment<DatepickerBottomSheetProfileBirthdayBinding>(R.layout.datepicker_bottom_sheet_profile_birthday) {

    val liveButton = MutableLiveData<String>()

    private var dateYear: String = ""
    private var dateMonth: String = ""
    private var dateDay: String = ""

    private var clickOk: ((String, String, String) -> Unit)? = null

    companion object {
        fun getInstance() = DatePickekProfileBirthDayBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun DatepickerBottomSheetProfileBirthdayBinding.onBind() {
        vi = this@DatePickekProfileBirthDayBottomSheetDialogFragment
        liveButton.postValue(context?.getString(R.string.ok))

        val today = Calendar.getInstance()

        val yearList = Array<String>(101) {
            i -> "${i}년"
        }
        val monthList = Array<String>(12) {
                i -> "${i}월"
        }
        val thisDaysOfMonth = getDaysInMonth(today.get(Calendar.MONTH) + 1, today.get(Calendar.YEAR))
        val dayList = Array<String>(thisDaysOfMonth) {
            i -> "${i}일"
        }

        Log.e("thisDaysOfMonth = $thisDaysOfMonth")

        for (i in 0..100) yearList[i] = "${i+1950}년"
        for (i in 0..11) monthList[i] = "${i+1}월"
        for (i in 0 until thisDaysOfMonth) {
            dayList[i] = "${i + 1}일"
            Log.e("dayList = ${dayList[i]}")
        }
        Log.e("dayList = ${dayList.size}")

        binding.profileYearSpinner.apply {
            minValue = 1950
            maxValue = 2050
            wrapSelectorWheel = false
            displayedValues = yearList
            value = today.get(Calendar.YEAR)
        }

        binding.profileMonthSpinner.apply {
            minValue = 1
            maxValue = 12
            wrapSelectorWheel = false
            displayedValues = monthList
            value = today.get(Calendar.MONTH)+1
        }

        binding.profileDaySpinner.apply {
            minValue = 1
            maxValue = thisDaysOfMonth
            wrapSelectorWheel = false
            displayedValues = dayList
            value = today.get(Calendar.DAY_OF_MONTH)
        }



    }

    private fun getDaysInMonth(month: Int, year: Int): Int{
        return when (month - 1) {
            Calendar.JANUARY, Calendar.MARCH, Calendar.MAY, Calendar.JULY, Calendar.AUGUST, Calendar.OCTOBER, Calendar.DECEMBER -> 31
            Calendar.APRIL, Calendar.JUNE, Calendar.SEPTEMBER, Calendar.NOVEMBER -> 30
            Calendar.FEBRUARY -> if (year % 4 == 0 && year % 100 != 0 || year % 400 == 0) 29 else 28
            else -> throw IllegalArgumentException("Invalid Month")
        }
    }

    fun setOnClickOk(clickOk: ((String, String, String) -> Unit)): DatePickekProfileBirthDayBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        dateYear = binding.profileYearSpinner.value.toString()
        dateMonth = binding.profileMonthSpinner.value.toString()
        dateDay = binding.profileDaySpinner.value.toString()
        clickOk?.invoke(dateYear, dateMonth, dateDay)
        dismiss()
    }
    fun onClickCancel(){
        dismiss()
    }


}
