package com.makeus.milliewillie.ui.info

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.MyApplication.Companion.IS_GOAL
import com.makeus.milliewillie.MyApplication.Companion.isInputGoal
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoAccountBinding
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.util.Log
import com.makeus.milliewillie.util.SharedPreference
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel

class AccountActivity: BaseDataBindingActivity<ActivityInfoAccountBinding>(R.layout.activity_info_account) {

    private val viewModel by viewModel<AccountViewModel>()

    override fun ActivityInfoAccountBinding.onBind() {
        vi = this@AccountActivity
        vm = viewModel
    }


    fun onClickWithDrawUser() {
        viewModel.apiRepository.deleteUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("deleteUsers 호출 성공")
                } else {
                    Log.e("deleteUsers 호출 실패")
                    Log.e(it.message)
                }
                isInputGoal = false
                SharedPreference.putSettingBooleanItem(
                    IS_GOAL,
                    isInputGoal
                )
                ActivityNavigator.with(this).welcome().start()
            }.disposeOnDestroy(this)
    }

    fun onClickUserRules() {
        ActivityNavigator.with(this).rulesInAccount().start()
    }

}