package com.makeus.milliewillie.ui.holiday

import android.view.View
import androidx.fragment.app.Fragment
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.recycler.BaseDataBindingRecyclerViewAdapter
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.*
import com.makeus.milliewillie.model.HolidayItem
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.view.CircleIndicator
import kotlinx.android.synthetic.main.activity_holiday.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.fragment_make_holiday.*
import kotlinx.android.synthetic.main.item_holiday.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class HolidayActivity : BaseDataBindingActivity<ActivityHolidayBinding>(R.layout.activity_holiday) {

    val viewModel by viewModel<HoliViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    override fun ActivityHolidayBinding.onBind() {
        vi = this@HolidayActivity
        vm = viewModel
        viewModel.bindLifecycle(this@HolidayActivity)

        rvHoli.isNestedScrollingEnabled = false
        holi1Indicator.customCreateDotPanel(
            8,
            R.drawable.indicator_dot_on,
            R.drawable.indicator_dot_off
        )
        holi2Indicator.customCreateDotPanel(
            8,
            R.drawable.indicator_dot_on,
            R.drawable.indicator_dot_off
        )
        holi3Indicator.customCreateDotPanel(
            8,
            R.drawable.indicator_dot_on,
            R.drawable.indicator_dot_off
        )
        holiPrizeIndicator.customCreateDotPanel(
            15,
            R.drawable.indicator_dot_off,
            R.drawable.indicator_dot_on
        )
        rvHoli.run {
            adapter = BaseDataBindingRecyclerViewAdapter<HolidayItem>()
                .addViewType(
                    BaseDataBindingRecyclerViewAdapter.MultiViewType<HolidayItem, ItemHolidayBinding>(
                        R.layout.item_holiday
                    ) {
                        vi = this@HolidayActivity


//                        viewModel.pickableMax = it.allHolidays.toInt()
//                        it.useHolidays = it.useHolidays + "일"
//                        it.allHolidays = " /" + it.allHolidays + "일"
//                        item = it

                    })

        }
    }

    fun onClickInfo() {

    }

    fun onClickOtherUse(str: String) {
        //title
        HolidayNumberPickerBottomSheetFragment.getInstance()
            .setOnClickOk {
                if (it.toInt() == viewModel.pickableMax) {
                    holiIndicator.selectDots(it.toInt())
                    btn_use.visibility = View.INVISIBLE
                }
                txt_use.text = it + "일"
                holiIndicator.selectDots(it.toInt())

            }
    }

    fun onClickUse(num: Int) {
        HolidayNumberPickerBottomSheetFragment.getInstance()
            .setOnClickOk {
                when (num) {
                    0 -> {
                        if (it.toInt() == 8) {
                            holi1Indicator.selectDots(it.toInt())
                            btn_use1.visibility = View.INVISIBLE
                            btn_use2.visibility = View.VISIBLE
                        }
                        textView4.text = it + "일"
                        holi1Indicator.selectDots(it.toInt())
                    }
                    1 -> {
                        if (it.toInt() == 8) {
                            holi1Indicator.selectDots(it.toInt())
                            btn_use2.visibility = View.INVISIBLE
                            btn_use3.visibility = View.VISIBLE
                        }
                        textView8.text = it + "일"
                        holi2Indicator.selectDots(it.toInt())
                    }
                    2 -> {
                        if (it.toInt() == 8) {
                            holi1Indicator.selectDots(it.toInt())
                            btn_use3.visibility = View.INVISIBLE
                        }
                        regularholi3.text = it + "일"
                        holi3Indicator.selectDots(it.toInt())
                    }
                }
                when (num) {
//                    1-> viewModel.usersRequest.endDate = viewModel.dateChangeTest(it)
//                    2-> viewModel.usersRequest.strPrivate = viewModel.dateChangeTest(it)
//                    3-> viewModel.usersRequest.strCorporal = viewModel.dateChangeTest(it)
//                    4 -> viewModel.usersRequest.strSergeant = viewModel.dateChangeTest(it)
                }
            }.show(supportFragmentManager)
    }

    fun onClickHoliAdd() {
        changeFragment(MakeHolidayFragment.getInstance())
    }

    fun onClickRegister() {
        viewModel.pickableMax = 15
        HolidayNumberPickerBottomSheetFragment.getInstance()
            .setOnClickOk {
                if (it.toInt() >= 1) {
                    btn_register_prize.visibility = View.INVISIBLE
                    btn_use_prize.visibility = View.VISIBLE
                }
                txt_holi_prize.text = it + "일"
                viewModel.livePrizeHoliday.postValue(it + "일 ")
                holiPrizeIndicator.selectDots(it.toInt())
            }
            .show(supportFragmentManager)
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.holi_container, fragment)
            .addToBackStack(null)
            .commit()
    }


    override fun onResume() {
        super.onResume()

    }

}