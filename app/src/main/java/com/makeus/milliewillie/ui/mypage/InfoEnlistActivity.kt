package com.makeus.milliewillie.ui.mypage

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoEnlistBinding
import com.makeus.milliewillie.ext.bgTint
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.ui.intro.UserViewModel
import com.makeus.milliewillie.ui.plan.ColorPickerBottomSheetFragment
import com.makeus.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_info_enlist.*
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.*
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.btn_discharge
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.layout_enlistSet
import kotlinx.android.synthetic.main.activity_make_plan.*
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class InfoEnlistActivity : BaseDataBindingActivity<ActivityInfoEnlistBinding>(R.layout.activity_info_enlist) {

    val viewModel by viewModel<MyPageEditViewModel>()

    override fun ActivityInfoEnlistBinding.onBind() {
        vi= this@InfoEnlistActivity
        vm= viewModel
        viewModel.bindLifecycle(this@InfoEnlistActivity)
    }


    fun onClickDate(position: Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)
                when(position){
                    0-> viewModel.usersResponse.startDate= viewModel.dateChangeTest(it)
                    1-> viewModel.usersResponse.endDate = viewModel.dateChangeTest(it)
                    2-> viewModel.usersResponse.strPrivate = viewModel.dateChangeTest(it)
                    3-> viewModel.usersResponse.strCorporal = viewModel.dateChangeTest(it)
                    4 -> viewModel.usersResponse.strSergeant = viewModel.dateChangeTest(it)
                }
            }.show(supportFragmentManager)
    }

    fun onClickType(){
        ServiceTypeBottomSheetDialogFragment.getInstance()
            .setOnClickType {
                viewModel.liveServicetype.postValue(it)
                viewModel.usersPatch.serveType= it
            }
            .show(supportFragmentManager)
    }
    fun onClickDone(){
        if(!viewModel.enlistValueTest()){
            Snackbar.make(this.layout_info,"날짜 정보를 확인해주세요.",Snackbar.LENGTH_SHORT).show()
        }
        else{
        patchUsers()}
    }
    fun patchUsers(){
        viewModel.patchUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if(it.isSuccess){
                    SampleToast.createToast(this,"일정 수정이 완료되었습니다.")?.show()
                    finish()
                }
                else{
                    SampleToast.createToast(this, "일정 수정실패")
                }
            },{})
    }

    override fun onResume() {
        super.onResume()
    }
}