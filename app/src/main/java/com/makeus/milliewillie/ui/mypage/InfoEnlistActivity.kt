package com.makeus.milliewillie.ui.mypage

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoEnlistBinding
import com.makeus.milliewillie.ext.bgTint
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.ui.plan.ColorPickerBottomSheetFragment
import com.makeus.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_info_enlist.*
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.*
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.btn_discharge
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.layout_enlistSet
import kotlinx.android.synthetic.main.activity_make_plan.*
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class InfoEnlistActivity : BaseDataBindingActivity<ActivityInfoEnlistBinding>(R.layout.activity_info_enlist) {

    val viewModel by viewModel<UserViewModel>()

    override fun ActivityInfoEnlistBinding.onBind() {
        vi= this@InfoEnlistActivity
        vm= viewModel

    }

    fun onClickDate(position: Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)
                when(position){
                    0-> viewModel.usersRequest.startDate= viewModel.dateChangeTest(it)
                    1-> viewModel.usersRequest.endDate = viewModel.dateChangeTest(it)
                    2-> viewModel.usersRequest.strPrivate = viewModel.dateChangeTest(it)
                    3-> viewModel.usersRequest.strCorporal = viewModel.dateChangeTest(it)
                    4 -> viewModel.usersRequest.strSergeant = viewModel.dateChangeTest(it)
                }
            }.show(supportFragmentManager)
    }

    fun onClickType(){
        ServiceTypeBottomSheetDialogFragment.getInstance()
            .setOnClickType {
                viewModel.liveServicetype.postValue(it)
                viewModel.usersRequest.serveType= it
            }
            .show(supportFragmentManager)
    }
    fun onClickDone(){
        if (viewModel.enlistValueTest()) {
            viewModel.calDday(btn_discharge.text.toString()).toString()
            finish()
        }
        else{
            Snackbar.make(this.layout_info ,"날짜 정보가 옳바르지 않습니다.", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.liveServicetype.value=viewModel.usersRequest.serveType
    }
}