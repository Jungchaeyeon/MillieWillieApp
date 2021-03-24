package com.makeus.milliewillie.ui.holiday

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.FragmentMakeHolidayBinding
import com.makeus.milliewillie.model.HolidayItem
import com.makeus.milliewillie.model.Plan
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_holiday.*
import kotlinx.android.synthetic.main.fragment_make_holiday.*
import kotlinx.android.synthetic.main.fragment_mypage_edit.*
import kotlinx.android.synthetic.main.fragment_mypage_edit.mypage_modify_container
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel


class MakeHolidayFragment :
    BaseDataBindingFragment<FragmentMakeHolidayBinding>(R.layout.fragment_make_holiday) {
    val viewModel: HoliViewModel by sharedViewModel()
    val repositoryCached by inject<RepositoryCached>()


    companion object {
        fun getInstance() = MakeHolidayFragment()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragment_make_holi.setOnTouchListener { p0, p1 -> true }

    }
    override fun FragmentMakeHolidayBinding.onBind() {
        vi = this@MakeHolidayFragment
        vm = viewModel

        input_holiday.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (input_holiday.text.toString().isNotEmpty()) {

                   input_holiday.text.clear()
                }
                return@setOnKeyListener true
            }
            false
        }
        input_add_holiday.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                if (input_add_holiday.text.toString().isNotEmpty()) {

                    input_add_holiday.text.clear()
                }
                return@setOnKeyListener true
            }
            false
        }

    }

    fun onClickCancle() {

        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.detach(this)
            ?.commit()

    }

    fun onClickDone() {
        if(edt_holi_name.text.isNullOrEmpty()){
            Snackbar.make(this.fragment_make_holi,"휴가 이름을 설정해주세요",Snackbar.LENGTH_SHORT).show()
        }else{
//            viewModel.holidayItem.holiTitle = edt_holi_name.text.toString()
//            viewModel.holidayItem.allHolidays = input_holiday.text.toString()
            //sheet형식 대로 저장

            onClickCancle()
        }

    }

    fun onClickHoliType(view: View) {
        when (view.id) {
            R.id.btn_holi_regular -> {
                viewModel.holidayItem.holiType="정기"
                btn_holi_regular.isChecked= true
                btn_holi_prize.isChecked = false
                btn_holi_other.isChecked = false
                layout_info_army_date.visibility=View.VISIBLE
                add_holiday.visibility=View.GONE
            }
            R.id.btn_holi_prize -> {
                viewModel.holidayItem.holiType="포상"
                btn_holi_prize.isChecked= true
                btn_holi_regular.isChecked = false
                btn_holi_other.isChecked = false
                layout_info_army_date.visibility=View.GONE
                add_holiday.visibility=View.VISIBLE
            }
            R.id.btn_holi_other -> {
                viewModel.holidayItem.holiType="기타"
                btn_holi_other.isChecked= true
                btn_holi_regular.isChecked = false
                btn_holi_prize.isChecked = false
                layout_info_army_date.visibility=View.GONE
                add_holiday.visibility=View.GONE
            }

        }
    }


}
