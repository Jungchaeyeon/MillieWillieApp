package com.makeus.milliewillie.ui.holiday

import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_holiday.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HolidayActivity : BaseDataBindingActivity<ActivityHolidayBinding>(R.layout.activity_holiday) {

    val viewModel by viewModel<HoliViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    override fun ActivityHolidayBinding.onBind() {
        vi = this@HolidayActivity
        vm = viewModel
        viewModel.bindLifecycle(this@HolidayActivity)

        holiRegulIndicator.customCreateDotPanel(
            viewModel.regularHoliNum,
            R.drawable.indicator_dot_on,
            R.drawable.indicator_dot_off
        )
        holiPrizeIndicator.customCreateDotPanel(
            viewModel.prizeHoliNum,
            R.drawable.indicator_dot_on,
            R.drawable.indicator_dot_off
        )
        holiOtherIndicator.customCreateDotPanel(
            viewModel.otherHoliNum,
            R.drawable.indicator_dot_on,
            R.drawable.indicator_dot_off
        )
    }

    fun onClickInfo() {

    }

    fun onClickUse(num: Int) {
        HolidayNumberPickerBottomSheetFragment.getInstance()
            .setOnClickOk {
                when (num) {
                    0 -> {
                        viewModel.pickableMax = viewModel.regularHoliNum
                        if (it.toInt() == viewModel.regularHoliNum) {
                            holi_regul_Indicator.selectDots(it.toInt())
                            btn_use1.visibility = View.INVISIBLE
                        }
                        else{
                        viewModel.liveRegularHoliday.postValue(it+"일")
                        //textView4.text = it + "일"
                        holi_regul_Indicator.selectDots(it.toInt())}
                    }
//                    1 -> {
//                        if (it.toInt() == viewModel.prizeHoliNum) {
//                            holi_prize_indicator.selectDots(it.toInt())
//                            btn_use_prize.visibility = View.INVISIBLE
//                            btn_register_prize.visibility = View.VISIBLE
//                        }
//                        //textView8.text = it + "일"
//                        viewModel.livePrizeHoliday.postValue(it+"일")
//                        holi_prize_indicator.selectDots(it.toInt())
//                    }
//                    2 -> {
//                        if (it.toInt() == viewModel.otherHoliNum) {
//                            holi_other_Indicator.selectDots(it.toInt())
//                            btn_register_other.visibility = View.INVISIBLE
//                            btn_use_other.visibility = View.INVISIBLE
//                        }
//                        //regularholi3.text = it + "일"
//                        viewModel.liveOtherHoliday.postValue(it+"일")
//                        holi_other_Indicator.selectDots(it.toInt())
//                    }
                }
            }.show(supportFragmentManager)
    }

    fun onClickRegister(num : Int) {

        HolidayNumberPickerBottomSheetFragment.getInstance()
            .setOnClickOk {

                when(num){
                    0-> {
                        //포상
                        viewModel.pickableMax = viewModel.prizeHoliNum
                        if (it.toInt() >= 1) {
                            btn_register_prize.visibility = View.INVISIBLE
                            btn_use_prize.visibility = View.VISIBLE
                        }
                        // txt_holi_prize.text = it + "일"
                        viewModel.livePrizeHoliday.postValue(it + "일 ")
                        holi_prize_indicator.selectDots(it.toInt())
                    }
                    1->{
                        if (it.toInt() >= 1) {
                            viewModel.pickableMax = viewModel.otherHoliNum
                            btn_register_other.visibility = View.INVISIBLE
                            btn_use_other.visibility = View.VISIBLE
                        }
                        viewModel.liveOtherHoliday.postValue(it + "일 ")
                        holi_other_Indicator.selectDots(it.toInt())
                    }
                }

            }
            .show(supportFragmentManager)
    }


}