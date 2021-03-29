package com.makeus.milliewillie.ui.intro

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroGoalBinding
import com.makeus.milliewillie.ext.showLongToastSafe
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_goal.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class IntroGoalActivity :
    BaseDataBindingActivity<ActivityIntroGoalBinding>(R.layout.activity_intro_goal) {

    val viewModel by viewModel<UserViewModel>()
    val context = this
    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.usersRequest =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as UsersRequest
    }
    override fun ActivityIntroGoalBinding.onBind() {
        vi = this@IntroGoalActivity
//        Log.e(viewModel.usersRequest.startDate.toString(),"startDate")
//        Log.e(viewModel.usersRequest.endDate.toString(),"endDate")
//        Log.e(viewModel.usersRequest.proDate.toString(),"proDate")
//        Log.e(viewModel.usersRequest.strCorporal.toString(),"strCorporal")
//        Log.e(viewModel.usersRequest.strPrivate.toString(),"strPrivate")
//        Log.e(viewModel.usersRequest.strSergeant.toString(),"strSergeant")
    }

    fun onClickBack() {
        onBackPressed()
    }

    fun onClickDone() {
        if (edt_goal.text.isNotEmpty()) {
            viewModel.usersRequest.goal=edt_goal.text.toString()
            requestUser()
        } else {
            Snackbar.make(this.linearLayout,"목표를 입력해 주세요.",Snackbar.LENGTH_SHORT).show()
        }
    }
    fun requestUser() {
        viewModel.requestUser().subscribe {
            if (it.isSuccess) {
              //  SampleToast.createToast(this,"밀리윌리 가입을 환영합니다 :)",).show()
                ActivityNavigator.with(this).main().start()
                showSuccessToast()
            } else {
                "가입 실패".showShortToastSafe()
            }
        }.disposeOnDestroy(this)
    }
    fun showSuccessToast(){
     //   SampleToast.createToast(context, "밀리윌리 가입을 환영합니다 :)")?.show()
    }


}