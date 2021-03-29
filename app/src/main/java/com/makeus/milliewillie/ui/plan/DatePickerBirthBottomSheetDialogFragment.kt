package com.makeus.milliewillie.ui.fragment

import android.icu.util.ChineseCalendar
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.DatepickerBottomSheetBirthBinding
import java.text.SimpleDateFormat
import java.util.*

class DatePickerBirthBottomSheetDialogFragment:
    BaseDataBindingBottomSheetFragment<DatepickerBottomSheetBirthBinding>(R.layout.datepicker_bottom_sheet_birth) {

    private var solarBirthday : String = ""
    private var lunarBirthday : String = ""
    private var thisYearLunarBirthday : String = ""
    private var nextYearLunarBirthday : String = ""
    private var gapDays : String = ""

    private var clickOk: ((String, String,String, String, String) -> Unit)? = null

    companion object {
        fun getInstance() = DatePickerBirthBottomSheetDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun DatepickerBottomSheetBirthBinding.onBind() {
        vi = this@DatePickerBirthBottomSheetDialogFragment

        val today = Calendar.getInstance()
        dpSpinner.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)) { view, year, month, day ->
            gapDays =
                when {
                    day > today.get(Calendar.DAY_OF_MONTH) -> "D - ${1 + getGapOfDays(year, month, day)}" // 오늘보다 이후 일 때
                    day < today.get(Calendar.DAY_OF_MONTH) -> "+ ${-getGapOfDays(year, month, day)}" // 오늘보다 이전 일 때
                    else -> "오늘" // 오늘 일 때
                }

            solarBirthday = "양력 생일 ${year}년 ${month + 1}월 ${day}일"
//            lunarBirthday = "음력 생일 ${convertLunarToSolar("$year${month+1}$day", false)}"
//            thisYearLunarBirthday = "올해 음력 생일 ${today.get(Calendar.YEAR)}년 ${convertLunarToSolar("$year${month+1}$day", true)}"
//            nextYearLunarBirthday = "내년 음력 생일 ${today.get(Calendar.YEAR)}년 ${convertLunarToSolar("$year${month+1}$day", true)}"
        }
    }

    fun getGapOfDays(year: Int, month: Int, day: Int): Long {
        val today = Calendar.getInstance().apply {
            set(Calendar.YEAR, get(Calendar.YEAR))
            set(Calendar.MONTH, get(Calendar.MONTH))
            set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH))
        }.timeInMillis

        val selectedDay = Calendar.getInstance().apply {
            set(Calendar.YEAR, get(Calendar.YEAR))
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

    fun getDateByString(date: Date, nonYear: Boolean): String {
        var sdf = SimpleDateFormat()

        when (nonYear) {
            true -> sdf = SimpleDateFormat("yy년 MM월 dd일")
            false -> sdf = SimpleDateFormat("MM월 dd일")
        }
        return sdf.format(date);
    }

    /**
     * 음력날짜를 양력날짜로 변환
     * @param 음력날짜 (yyyyMMdd)
     * @return 양력날짜 (yyyyMMdd)
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun convertLunarToSolar(date: String, nonYear: Boolean): String {
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()

        cc.set(ChineseCalendar.EXTENDED_YEAR, Integer.parseInt(date.substring(0, 4)) + 2637)
        cc.set(ChineseCalendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1)
        cc.set(ChineseCalendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)))

        cal.setTimeInMillis(cc.getTimeInMillis())
        return getDateByString(cal.getTime(), nonYear)
    }

    /**
     * 양력날짜를 음력날짜로 변환
     * @param 양력날짜 (yyyyMMdd)
     * @return 음력날짜 (yyyyMMdd)
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun converSolarToLunar(date: String, nonYear: Boolean): String {
        val cc = ChineseCalendar()
        val cal = Calendar.getInstance()

        cal.set(Calendar.YEAR, Integer.parseInt(date.substring(0, 4)))
        cal.set(Calendar.MONTH, Integer.parseInt(date.substring(4, 6)) - 1)
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.substring(6)))

        cc.setTimeInMillis(cal.getTimeInMillis())

        val y = cc.get(ChineseCalendar.EXTENDED_YEAR) - 2637
        val m = cc.get(ChineseCalendar.MONTH) + 1
        val d = cc.get(ChineseCalendar.DAY_OF_MONTH)

        val ret = StringBuffer()
        when (nonYear) {
            true -> {
                ret.append(String.format("%02d", m)).append("-")
                ret.append(String.format("%02d", d))
            }
            false -> {
                ret.append(String.format("%04d", y)).append("-")
                ret.append(String.format("%02d", m)).append("-")
                ret.append(String.format("%02d", d))
            }
        }

        return ret.toString()
    }

    fun setOnClickOk(clickOk: ((String, String,String, String, String) -> Unit)): DatePickerBirthBottomSheetDialogFragment {
        this.clickOk = clickOk
        return this
    }

    fun onClickOk() {
        if (solarBirthday.isBlank()) {
            solarBirthday = "${Calendar.getInstance().get(Calendar.YEAR)}년 ${Calendar.getInstance().get(Calendar.MONTH)+1}월 ${Calendar.getInstance().get(Calendar.DAY_OF_MONTH)}일"
            gapDays = "오늘"
        }

        clickOk?.invoke(solarBirthday, gapDays, lunarBirthday, thisYearLunarBirthday, nextYearLunarBirthday)
        dismiss()
    }
    fun onClickCancel(){
        dismiss()
    }




}