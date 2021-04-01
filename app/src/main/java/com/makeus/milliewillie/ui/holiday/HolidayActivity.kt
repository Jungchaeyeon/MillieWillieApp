package com.makeus.milliewillie.ui.holiday

import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_holiday.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.routine_ex_bottom_sheet.*
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HolidayActivity : BaseDataBindingActivity<ActivityHolidayBinding>(R.layout.activity_holiday) {

    val viewModel by viewModel<HoliViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    var regulUse = 0
    var prizeUse = 0
    var otherUse = 0
    var regulAvail = 0
    var prizeAvail = 0
    var prizeAllNum =0
    var otherAvail = 0
    var otherAllNum =0

    var maxValue = 24
    val context = this
    var infoFlag = false
    var firstRegul = true
    override fun ActivityHolidayBinding.onBind() {
        vi = this@HolidayActivity
        vm = viewModel
        viewModel.bindLifecycle(this@HolidayActivity)
    }

    inner class PopupListener : PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.menu1 -> ActivityNavigator.with(context)
                    .holiedit(viewModel.liveHoliType.value.toString()).start()
            }

            return false
        }
    }

    // 3개점 땡땡이 INFO
    fun onClickInfo(view: View) {
        // openContextMenu(view)
        when (view.id) {
            R.id.regular_edit -> {
                viewModel.liveHoliType.value = "정기휴가"
                repositoryCached.setValue(
                    LocalKey.PATCHVACID,
                    viewModel.vacationIdResponse.result[0].vacationId
                )
            }
            R.id.prize_edit -> {
                viewModel.liveHoliType.value = "포상휴가"
                repositoryCached.setValue(
                    LocalKey.PATCHVACID,
                    viewModel.vacationIdResponse.result[1].vacationId
                )
            }
            R.id.other_edit -> {
                viewModel.liveHoliType.value = "기타휴가"
                repositoryCached.setValue(
                    LocalKey.PATCHVACID,
                    viewModel.vacationIdResponse.result[2].vacationId
                )
            }

        }
        val popup = PopupMenu(this, view)
        val inflate = popup.menuInflater
        inflate.inflate(R.menu.holi_edit_menu, popup.menu)
        popup.setOnMenuItemClickListener(PopupListener())
        popup.show()
    }
    fun onClickDone(){

    }

    //사용하기 버튼 눌렀을 때
    fun onClickUse(num: Int) {
        //선택 버튼 종류
        when (num) {
            0 -> viewModel.pickableMax = viewModel.regularHoliNum - regulUse
            1 -> viewModel.pickableMax = viewModel.prizeHoliNum - prizeUse
            2 -> viewModel.pickableMax = viewModel.otherHoliNum - otherUse
        }
        HolidayNumberPickerBottomSheetFragment.getInstance()
            .setOnClickOk {
                when (num) {
                    0 -> {
                        regulUse += it.toInt()
                        viewModel.liveRegularHoliday.postValue(regulUse.toString() + "일 /")
                        calRegulIndicator(regulUse)
                        Log.e(viewModel.vacationIdPatch.useDays.toString(), "사용일수")
                        Log.e(viewModel.vacationIdPatch.totalDays.toString(), "총 사용일수")
                        viewModel.vacationIdPatch.useDays = it.toInt()
                        repositoryCached.setValue(LocalKey.PATCHVACID,repositoryCached.getVac1Id())
                        viewModel.patchVacationUseDays()
                    }
                    1 -> {
                        if (it.toInt() == viewModel.prizeHoliNum) {
                            // 전체 휴가일수 == 사용휴가일 수
                             btn_use_prize.visibility = View.INVISIBLE
                             btn_register_prize.visibility = View.VISIBLE
                        }
                            //사용자가 선택한 날 +"일"

                        prizeUse += it.toInt()
                        viewModel.livePrizeHoliday.postValue(prizeUse.toString() + "일/")
                        calPrizeIndicator(prizeUse)
                        //vacationIdPatch
                        //viewModel 에 전체/ 사용일 수 기록
                        Log.e(viewModel.vacationIdPatch.useDays.toString(), "사용일수")
                        Log.e(viewModel.vacationIdPatch.totalDays.toString(), "총 사용일수")
                        viewModel.vacationIdPatch.useDays = it.toInt()
                        repositoryCached.setValue(LocalKey.PATCHVACID,repositoryCached.getVac2Id())
                        viewModel.patchVacationUseDays()
                    }
                    2 -> {
                        if (it.toInt() == viewModel.otherHoliNum) {
                            // 전체 휴가일수 == 사용휴가일 수
                                btn_register_other.visibility = View.INVISIBLE
                                btn_use_other.visibility = View.VISIBLE
                            }
                            //regularholi3.text = it + "일"
                            otherUse += it.toInt()
                            viewModel.liveOtherHoliday.postValue(otherUse.toString() + "일/")
                            calOtherIndicator(otherUse)
                        //vacationIdPatch
                        Log.e(viewModel.vacationIdPatch.useDays.toString(), "사용일수")
                        Log.e(viewModel.vacationIdPatch.totalDays.toString(), "총 사용일수")
                        viewModel.vacationIdPatch.useDays = it.toInt()
                        repositoryCached.setValue(LocalKey.PATCHVACID,repositoryCached.getVac3Id())
                        viewModel.patchVacationUseDays()
                    }
                }
            }.show(supportFragmentManager)
    }

    //사용하기
    fun calRegulIndicator(num: Int) {

        val countRest: Int = num.rem(12)
        val availRest : Int = regulAvail.rem(12)
        Log.e("$countRest", "countRest")
        Log.e((regulAvail - regulUse).toString(), "count차")
        Log.e(regulAvail.toString(), "Avail")
        Log.e(regulUse.toString(), "use")
        Log.e("$countRest", "countRest")


        when (num) {
            in 1..11 -> {
                holi_regul_Indicator1.selectDotsTwice(countRest, regulAvail )
                holi_regul_Indicator2.selectDotsTwice(0, regulAvail - 12)
                if(regulAvail>24){
                    holi_regul_Indicator3.selectDotsTwice(0, availRest)}
                else{
                    holi_regul_Indicator3.selectDotsTwice(0, 0)}
            }
            in 12..23 -> {
                holi_regul_Indicator1.selectDotsTwice(12, 0)
                if(regulAvail>=24){
                    holi_regul_Indicator2.selectDotsTwice(countRest, 12)
                    holi_regul_Indicator3.selectDotsTwice(0, availRest)}
                else{
                    holi_regul_Indicator2.selectDotsTwice(countRest, availRest)
                    holi_regul_Indicator3.selectDotsTwice(0, 0)}
            }
            in 24..35-> {
                holi_regul_Indicator1.selectDotsTwice(12, 0)
                holi_regul_Indicator2.selectDotsTwice(12, 0)
                holi_regul_Indicator3.selectDotsTwice(countRest, regulAvail-24)
            }
        }
    }

    fun calPrizeIndicator(num: Int) {
        val countRest: Int = num.rem(12)
        val availRest : Int = prizeAllNum.rem(12)

        Log.e(num.toString(),"num값값값")
        Log.e(availRest.toString(),"avial테스트")
        Log.e(countRest.toString(),"avial")
        Log.e(prizeAllNum.toString(),"avial스트")



        when (num) {
            in 1..11 -> {
                holi_prize_indicator1.selectDotsTwice(countRest, prizeAllNum )
                holi_prize_Indicator2.selectDotsTwice(0, prizeAllNum - 12)
                if(prizeAllNum>24){
                    holi_prize_Indicator3.selectDotsTwice(0, availRest)}
                else{
                    holi_prize_Indicator3.selectDotsTwice(0, 0)}
            }
            in 12..23 -> {
                holi_prize_indicator1.selectDotsTwice(12, 0)
                if(prizeAllNum>=24){
                    holi_prize_Indicator2.selectDotsTwice(countRest, 12)
                    holi_prize_Indicator3.selectDotsTwice(0, availRest)}
                else{
                     holi_prize_Indicator2.selectDotsTwice(countRest, availRest)
                     holi_prize_Indicator3.selectDotsTwice(0, 0)
            }}
            in 24..36 -> {
                holi_prize_indicator1.selectDotsTwice(12, 0)
                holi_prize_Indicator2.selectDotsTwice(12, 0)
                holi_prize_Indicator3.selectDotsTwice(countRest, prizeAllNum - 24)

            }
            }
        }


    fun calOtherIndicator(num: Int) {
        val countRest: Int = num.toInt().rem(12)
        val availRest : Int = otherAllNum.rem(12)
        Log.e(num.toString(),"num값값값")
        Log.e(availRest.toString(),"otherAvail")
        Log.e(countRest.toString(),"COuntTEst")

        Log.e(otherAllNum.toString(),"OTHER")
        when (num) {
            in 1..11 -> {
                holi_other_Indicator1.selectDotsTwice(countRest, otherAllNum)
                holi_other_Indicator2.selectDotsTwice(0, otherAllNum - 12)
                if(otherAllNum>=24){
                     holi_other_Indicator3.selectDotsTwice(0, availRest)}
                else{
                    holi_other_Indicator3.selectDotsTwice(0, 0)}
            }
            in 12..23 -> {
                holi_other_Indicator1.selectDotsTwice(12, 0)
                if(otherAllNum>=24){
                    holi_other_Indicator2.selectDotsTwice(countRest, 12)
                    holi_other_Indicator3.selectDotsTwice(0, availRest)}
                else{
                    holi_other_Indicator2.selectDotsTwice(countRest, availRest)
                    holi_other_Indicator3.selectDotsTwice(0, 0)}

            }
            in 24..35 -> {
                holi_other_Indicator1.selectDotsTwice(12, 0)
                holi_other_Indicator2.selectDotsTwice(12, 0)
                holi_other_Indicator3.selectDotsTwice(countRest, otherAllNum - 24)
            }
        }
    }

    fun calRegisRegulIndicator(num: Int) {
        Log.e(num.toString(), "AVAIL")
        when (num.toInt()) {
            in 1..12 -> {
                holi_regul_Indicator1.selectDotsWhite(regulAvail)
                holi_regul_Indicator2.selectDotsWhite(0)
                holi_regul_Indicator3.selectDotsWhite(0)
            }
            in 13..24 -> {
                holi_regul_Indicator1.selectDotsWhite(12)
                holi_regul_Indicator2.selectDotsWhite(regulAvail - 12)
                holi_regul_Indicator3.selectDotsWhite(0)
            }
            in 24..35 -> {
                holi_regul_Indicator1.selectDotsWhite(12)
                holi_regul_Indicator2.selectDotsWhite(12)
                holi_regul_Indicator3.selectDotsWhite(regulAvail - 24)

            }
        }
    }

    fun calRegisPrizeRIndicator(num: Int) {

        when (num.toInt()) {
            in 1..12 -> {
                holi_prize_indicator1.selectDotsWhite(prizeAvail)
                holi_prize_Indicator2.selectDotsWhite(0)
                holi_prize_Indicator3.selectDotsWhite(0)
            }
            in 13..24 -> {
                holi_prize_indicator1.selectDotsWhite(12)
                holi_prize_Indicator2.selectDotsWhite(prizeAvail - 12)
                holi_prize_Indicator3.selectDotsWhite(0)
            }
            in 24..35 -> {
                holi_prize_indicator1.selectDotsWhite(12)
                holi_prize_Indicator2.selectDotsWhite(12)
                holi_prize_Indicator3.selectDotsWhite(prizeAvail - 24)

            }
        }
        viewModel.prizeHoliNum = num
        prizeAvail =num
    }

    fun calRegisOtherIndicator(num: Int) {

        when (num.toInt()) {
            in 1..11 -> {
                holi_other_Indicator1.selectDotsWhite(otherAvail)
                holi_other_Indicator2.selectDotsTwice(0, 0)
                holi_other_Indicator3.selectDotsTwice(0, 0)
            }
            in 13..23 -> {
                holi_other_Indicator1.selectDotsWhite(12)
                holi_other_Indicator2.selectDotsWhite(otherAvail - 12)
                holi_other_Indicator3.selectDotsWhite(0)
            }
            in 24..35 -> {
                holi_other_Indicator1.selectDotsWhite(12)
                holi_other_Indicator2.selectDotsWhite(12)
                holi_other_Indicator3.selectDotsWhite(otherAvail - 24)

            }
        }
        viewModel.otherHoliNum = num
        prizeAvail =num
    }

    fun onClickRegister(num: Int) {
                when (num) {
                    0 -> {
                        //포상 등록
                            viewModel.liveHoliType.value = "포상휴가"
                            repositoryCached.setValue(
                                LocalKey.PATCHVACID,
                                viewModel.vacationIdResponse.result[1].vacationId)

                        }

                    1 -> {
                        viewModel.liveHoliType.value = "기타휴가"
                        repositoryCached.setValue(
                        LocalKey.PATCHVACID,
                        viewModel.vacationIdResponse.result[2].vacationId
                        )
                    }
                }
            ActivityNavigator.with(this).holiedit(viewModel.liveHoliType.value.toString()).start()

    }

    fun onClickQues() {
        if (infoFlag == false) {
            txt_bubble_info.visibility = View.VISIBLE
            infoFlag = true
        } else {
            layout_holi_edit.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    txt_bubble_info.visibility = View.GONE
                    infoFlag = false
                    return true
                }
            })
        }

    }
    fun initIndicator() {
        if (viewModel.prizeNum != 0) btn_register_prize.visibility = View.GONE
        if (viewModel.otherNum != 0) btn_register_other.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        viewModel.getVacation {
            if (it) {

                regulUse = viewModel.regularNum
                prizeUse = viewModel.prizeNum
                otherUse = viewModel.otherNum
                regulAvail = viewModel.regularHoliNum
                prizeAvail = viewModel.prizeHoliNum
                otherAvail = viewModel.otherHoliNum


                Log.e(prizeUse.toString(), "prizeUse")
                Log.e(prizeAvail.toString(), "prizeAvail")
                prizeAllNum = prizeAvail
                otherAllNum = otherAvail
                holi_regul_Indicator1.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)
                holi_regul_Indicator2.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)
                holi_regul_Indicator3.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)

                holi_prize_indicator1.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)
                holi_prize_Indicator2.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)
                holi_prize_Indicator3.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)

                holi_other_Indicator1.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)
                holi_other_Indicator2.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)
                holi_other_Indicator3.customCreateDotPanel(R.drawable.indicator_dot_off,
                    R.drawable.indicator_dot_on,
                    0)
                if(regulUse==0 && regulAvail !=0) {calRegisRegulIndicator(regulAvail)
                    btn_use1.visibility=View.VISIBLE}

                if(prizeUse==0 && prizeAvail!=0) {calRegisPrizeRIndicator(prizeAvail)
                    btn_use_prize.visibility=View.VISIBLE
                    btn_register_prize.visibility=View.GONE}

                if(otherUse == 0 && otherAvail !=0 ){ calRegisOtherIndicator(otherAvail)
                    btn_use_other.visibility=View.VISIBLE
                    btn_register_other.visibility=View.GONE}
                if(prizeUse!=0) {
                    btn_register_prize.visibility = View.GONE
                    btn_use_prize.visibility=View.VISIBLE}
                if(otherUse!=0){ btn_register_other.visibility= View.GONE
                    btn_use_other.visibility=View.VISIBLE}
                if (repositoryCached.getFirstAccess() =="T") {
                    initIndicator()
                    repositoryCached.setValue(LocalKey.FIRSTACCESS,"F")
                } else {
                    Log.e("24일 아님")

                    calRegulIndicator(regulUse)
                    Log.e(regulAvail.toString(),"레귤러유즈숫자")
                    calPrizeIndicator(prizeUse)
                    calOtherIndicator(otherUse)

                    Log.e(viewModel.regularNum.toString(), "regularNum")
                    Log.e(viewModel.vacationIdResponse.result[1].useDays.toString(), "regularHoli")
                    Log.e(viewModel.otherHoliNum.toString(), "otherHoliNum")
                    Log.e(viewModel.vacationIdResponse.result[2].useDays.toString(), "regularHoli")
                }
            }
        }
    }
}