package com.makeus.milliewillie.ui.holiday

import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.PopupMenu
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
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
    var prizeRegis =0
    var otherRegis =0
    var maxValue =24
    val context = this
    var infoFlag= false
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
    inner class PopupListener: PopupMenu.OnMenuItemClickListener {

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item?.itemId) {
                R.id.menu1 ->  ActivityNavigator.with(context).holiedit(viewModel.liveHoliType.value.toString()).start()
            }

            return false
        }
    }

    fun onClickInfo(view:View) {
       // openContextMenu(view)
        when(view.id){
            R.id.regular_edit -> viewModel.liveHoliType.value = "정기휴가"
            R.id.prize_edit ->viewModel.liveHoliType.value = "포상휴가"
            R.id.other_edit ->viewModel.liveHoliType.value = "기타휴가"
        }
        val popup = PopupMenu(this, view)
        val inflate = popup.menuInflater
        inflate.inflate(R.menu.holi_edit_menu, popup.menu)
        popup.setOnMenuItemClickListener(PopupListener())
        popup.show()
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
                        } else {
                            viewModel.liveRegularHoliday.postValue(it + "일")
                            //textView4.text = it + "일"
                            holi_regul_Indicator.selectDots(it.toInt())
                        }
                    }
                    1 -> {
                        if (it.toInt() == viewModel.prizeHoliNum) {
                            holi_prize_indicator.selectDots(it.toInt())
                            btn_use_prize.visibility = View.INVISIBLE
                            btn_register_prize.visibility = View.VISIBLE
                        }

                        viewModel.livePrizeHoliday.postValue(it + "일")
                        holi_prize_indicator.selectDotsTwice(prizeRegis,it.toInt())
                    }
                    2 -> {
                        if (it.toInt() == viewModel.otherHoliNum) {
                            holi_other_Indicator.selectDots(it.toInt())
                            btn_register_other.visibility = View.INVISIBLE
                            btn_use_other.visibility = View.INVISIBLE
                        }
                        //regularholi3.text = it + "일"
                        viewModel.liveOtherHoliday.postValue(it + "일")
                        holi_other_Indicator.selectDotsTwice(otherRegis,it.toInt())
                    }
                }
            }.show(supportFragmentManager)
    }

    fun onClickRegister(num: Int) {

        HolidayNumberPickerBottomSheetFragment.getInstance()
            .setOnClickOk {

                when (num) {
                    0 -> {
                        //포상
                        viewModel.pickableMax = viewModel.prizeHoliNum
                        if (it.toInt() >= 1) {
                            btn_register_prize.visibility = View.INVISIBLE
                            btn_use_prize.visibility = View.VISIBLE
                        }
                        // txt_holi_prize.text = it + "일"
                        prizeRegis= it.toInt()
                        viewModel.livePrizeHoliday.postValue(it + "일 ")
                        holi_prize_indicator.selectDots(it.toInt())
                    }
                    1 -> {
                        if (it.toInt() >= 1) {
                            viewModel.pickableMax = viewModel.otherHoliNum
                            btn_register_other.visibility = View.INVISIBLE
                            btn_use_other.visibility = View.VISIBLE
                        }
                        otherRegis= it.toInt()
                        viewModel.liveOtherHoliday.postValue(it + "일 ")
                        holi_other_Indicator.selectDots(it.toInt())
                    }
                }

            }
            .show(supportFragmentManager)
    }
    fun onClickQues(){
        if(infoFlag == false)
        {txt_bubble_info.visibility =View.VISIBLE
        infoFlag =true}
        else{
            layout_holi_edit.setOnTouchListener(object : View.OnTouchListener{
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    txt_bubble_info.visibility = View.GONE
                    infoFlag = false
                    return true
                }
            })
        }

    }


}