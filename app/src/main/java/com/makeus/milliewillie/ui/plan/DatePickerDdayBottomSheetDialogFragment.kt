package com.makeus.milliewillie.ui.fragment

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetDDayBinding
import com.makeus.milliewillie.util.BasicTextFormat
import java.util.*

class DatePickerDdayBottomSheetDialogFragment:
    BaseDataBindingBottomSheetFragment<DatepickerBottomSheetDDayBinding>(R.layout.datepicker_bottom_sheet_d_day) {

    val liveButton = MutableLiveData<String>()

    private var date : String = ""
    private var dotDate : String = ""
    private var gapDays : String = ""
    private var dateYear: String = ""
    private var dateMonth: String = ""

    private var clickOk: ((String, String, String, String, String) -> Unit)? = null

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
            gapDays =
                when {
                    day > today.get(Calendar.DAY_OF_MONTH) -> "D - ${1 + getGapOfDays(year, month, day)}" // 오늘보다 이후 일 때
                    day < today.get(Calendar.DAY_OF_MONTH) -> "+ ${-getGapOfDays(year, month, day)}" // 오늘보다 이전 일 때
                    else -> "오늘" // 오늘 일 때
                }

            dotDate = BasicTextFormat.BasicDateFormat(year.toString(), (month + 1).toString(), day.toString())
            date = "${year}년 ${month + 1}월 ${day}일"
            dateYear = year.toString()
            dateMonth = (month + 1).toString()
        }
    }

    fun getGapOfDays(year: Int, month: Int, day: Int): Long {
        val today = Calendar.getInstance().apply {
            set(Calendar.YEAR, get(Calendar.YEAR))
            set(Calendar.MONTH, get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH))
        }.timeInMillis

        val selectedDay = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }.timeInMillis

        val fewDay = getIgnoredTime(selectedDay) - today

        return fewDay / (24 * 60 * 60 * 1000)
    }

    fun getIgnoredTime(time: Long): Long {
        return Calendar.getInstance().apply {
            timeInMillis = time

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    fun setOnClickOk(clickOk: ((String, String, String, String, String) -> Unit)): DatePickerDdayBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        if (date.isBlank()) {
            date = "${Calendar.getInstance().get(Calendar.YEAR)}년 ${Calendar.getInstance().get(Calendar.MONTH)+1}월 ${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}일"
            dotDate = BasicTextFormat.BasicDateFormat(Calendar.getInstance().get(Calendar.YEAR).toString(),(Calendar.getInstance().get(Calendar.MONTH) + 1).toString(), Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString())
            gapDays = "오늘"
            dateYear = "${Calendar.getInstance().get(Calendar.YEAR)}"
            dateMonth = "${Calendar.getInstance().get(Calendar.MONTH)+1}"
        }

        clickOk?.invoke(date, dotDate, gapDays, dateYear, dateMonth)
        dismiss()
    }
    fun onClickCancel(){
        dismiss()
    }


}