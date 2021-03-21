package com.makeus.milliewillie.ui.intro

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
import com.makeus.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_goal.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class IntroGoalActivity :
    BaseDataBindingActivity<ActivityIntroGoalBinding>(R.layout.activity_intro_goal) {

    val viewModel by viewModel<UserViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    override fun ActivityIntroGoalBinding.onBind() {
        vi = this@IntroGoalActivity
    }

    fun onClickBack() {
        onBackPressed()
    }

    fun onClickDone() {
        if (edt_goal.text.isNotEmpty()) {
            viewModel.liveUserGoal.postValue(edt_goal.text.toString())
            ActivityNavigator.with(this).main().start()
            //requestUserUpdate()
        } else {
            Snackbar.make(this.linearLayout,"목표를 입력해 주세요.",Snackbar.LENGTH_SHORT).show()
        }
    }
    fun requestKakao() {
        viewModel.requestKakao().subscribe {
            if (it.isSuccess) {
                "회원가입 완료".showShortToastSafe()
            } else {
                "회원가입 실패".showShortToastSafe()
            }
        }.disposeOnDestroy(this)
    }
    fun requestUserUpdate() {
        Log.e("1.requestUserUpdate호출")
        viewModel.requestUserUpdate().subscribe {
            if (it.isSuccess) {
                "변경 완료".showShortToastSafe()
            } else {
                "변경 실패".showShortToastSafe()
            }
        }.disposeOnDestroy(this)
    }

}