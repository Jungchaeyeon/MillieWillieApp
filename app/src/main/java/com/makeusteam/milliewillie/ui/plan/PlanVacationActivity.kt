package com.makeusteam.milliewillie.ui.plan

import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.*
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_holiday.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.activity_my_page_edit.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_home_layout.view.*
import kotlinx.android.synthetic.main.item_plan_todo.*
import kotlinx.android.synthetic.main.item_plan_todo.view.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class PlanVacationActivity :
    BaseDataBindingActivity<ActivityPlanVacationBinding>(R.layout.activity_plan_vacation) {

    private val viewModel by viewModel<MakePlanViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    val context = this
    private var regulUse = 0
    private var prizeUse = 0
    private var otherUse = 0
    private var regulAvail = 0
    private var prizeAvail = 0
    private var prizeAllNum = 0
    private var otherAvail = 0
    private var otherAllNum = 0

    companion object {
        fun getInstance() = PlanVacationActivity()
    }

//    override fun setupProperties(bundle: Bundle?) {
//        super.setupProperties(bundle)
//        viewModel.plansRequest = bundle?.getSerializable(ActivityNavigator.KEY_DATA) as PlansRequest
//    }

    override fun ActivityPlanVacationBinding.onBind() {
        vi = this@PlanVacationActivity
        vm = viewModel
        viewModel.bindLifecycle(this@PlanVacationActivity)

    }

    fun calRegulIndicator(num: Int) {

        val countRest: Int = num.rem(12)
        val availRest: Int = regulAvail.rem(12)
//        Log.e("$countRest", "countRest")
//        Log.e((regulAvail - regulUse).toString(), "count차")
//        Log.e(regulAvail.toString(), "Avail")
//        Log.e(regulUse.toString(), "use")
//        Log.e("$countRest", "countRest")


        when (num) {
            in 1..11 -> {
                holi_regul_Indicator1.selectDotsTwice(countRest, regulAvail)
                holi_regul_Indicator2.selectDotsTwice(0, regulAvail - 12)
                if (regulAvail > 24) {
                    holi_regul_Indicator3.selectDotsTwice(0, availRest)
                } else {
                    holi_regul_Indicator3.selectDotsTwice(0, 0)
                }
            }
            in 12..23 -> {
                holi_regul_Indicator1.selectDotsTwice(12, 0)
                if (regulAvail >= 24) {
                    holi_regul_Indicator2.selectDotsTwice(countRest, 12)
                    holi_regul_Indicator3.selectDotsTwice(0, availRest)
                } else {
                    holi_regul_Indicator2.selectDotsTwice(countRest, availRest)
                    holi_regul_Indicator3.selectDotsTwice(0, 0)
                }
            }
            in 24..35 -> {
                holi_regul_Indicator1.selectDotsTwice(12, 0)
                holi_regul_Indicator2.selectDotsTwice(12, 0)
                holi_regul_Indicator3.selectDotsTwice(countRest, regulAvail - 24)
            }
        }
    }

    fun calPrizeIndicator(num: Int) {
        val countRest: Int = num.rem(12)
        val availRest: Int = prizeAllNum.rem(12)

//        Log.e(num.toString(), "num값값값")
//        Log.e(availRest.toString(), "avial테스트")
//        Log.e(countRest.toString(), "avial")
//        Log.e(prizeAllNum.toString(), "avial스트")



        when (num) {
            in 1..11 -> {
                holi_prize_indicator1.selectDotsTwice(countRest, prizeAllNum)
                holi_prize_Indicator2.selectDotsTwice(0, prizeAllNum - 12)
                if (prizeAllNum > 24) {
                    holi_prize_Indicator3.selectDotsTwice(0, availRest)
                } else {
                    holi_prize_Indicator3.selectDotsTwice(0, 0)
                }
            }
            in 12..23 -> {
                holi_prize_indicator1.selectDotsTwice(12, 0)
                if (prizeAllNum >= 24) {
                    holi_prize_Indicator2.selectDotsTwice(countRest, 12)
                    holi_prize_Indicator3.selectDotsTwice(0, availRest)
                } else {
                    holi_prize_Indicator2.selectDotsTwice(countRest, availRest)
                    holi_prize_Indicator3.selectDotsTwice(0, 0)
                }
            }
            in 24..36 -> {
                holi_prize_indicator1.selectDotsTwice(12, 0)
                holi_prize_Indicator2.selectDotsTwice(12, 0)
                holi_prize_Indicator3.selectDotsTwice(countRest, prizeAllNum - 24)

            }
        }
    }


    fun calOtherIndicator(num: Int) {
        val countRest: Int = num.toInt().rem(12)
        val availRest: Int = otherAllNum.rem(12)
//        Log.e(num.toString(), "num값값값")
//        Log.e(availRest.toString(), "otherAvail")
//        Log.e(countRest.toString(), "COuntTEst")
//
//        Log.e(otherAllNum.toString(), "OTHER")
        when (num) {
            in 1..11 -> {
                holi_other_Indicator1.selectDotsTwice(countRest, otherAllNum)
                holi_other_Indicator2.selectDotsTwice(0, otherAllNum - 12)
                if (otherAllNum >= 24) {
                    holi_other_Indicator3.selectDotsTwice(0, availRest)
                } else {
                    holi_other_Indicator3.selectDotsTwice(0, 0)
                }
            }
            in 12..23 -> {
                holi_other_Indicator1.selectDotsTwice(12, 0)
                if (otherAllNum >= 24) {
                    holi_other_Indicator2.selectDotsTwice(countRest, 12)
                    holi_other_Indicator3.selectDotsTwice(0, availRest)
                } else {
                    holi_other_Indicator2.selectDotsTwice(countRest, availRest)
                    holi_other_Indicator3.selectDotsTwice(0, 0)
                }

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
        prizeAvail = num
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
        prizeAvail = num
    }
    fun onClickBack(){
        Log.e("확인")
        ActivityNavigator.with(this).makeplan().start()
    }
    fun onClickManage(){
        ActivityNavigator.with(this).holiday().start()
    }
    override fun onBackPressed() {
        ActivityNavigator.with(this).makeplan().start()
    }

    override fun onResume() {
        super.onResume()
        PlanVacationBottomSheetFragment.getInstance()
            .setOnClickOk {

                Log.e(viewModel.plansRequest.planVacation.toString())

                this.finish()
                // ActivityNavigator.with(this@PlanVacationActivity).makeplan().start()
            }
            .show(supportFragmentManager)
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
                if (regulUse == 0 && regulAvail != 0) {
                    calRegisRegulIndicator(regulAvail)
                }

                if (prizeUse == 0 && prizeAvail != 0) {
                    calRegisPrizeRIndicator(prizeAvail)
                }

                if (otherUse == 0 && otherAvail != 0) {
                    calRegisOtherIndicator(otherAvail)
                }

                if (repositoryCached.getFirstAccess() == "T") {
                    repositoryCached.setValue(LocalKey.FIRSTACCESS, "F")
                } else {
                    Log.e("24일 아님")

                    calRegulIndicator(regulUse)
                    Log.e(regulAvail.toString(), "레귤러유즈숫자")
                    calPrizeIndicator(prizeUse)
                    calOtherIndicator(otherUse)

                    Log.e(viewModel.regularNum.toString(), "regularNum")
                    Log.e(viewModel.otherHoliNum.toString(), "otherHoliNum")
                }

            }
        }
    }
}







