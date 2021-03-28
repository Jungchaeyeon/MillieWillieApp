package com.makeus.milliewillie.ui.info

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoAccountBinding
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.util.Log
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
                // 처음 화면으로 이동 추가해야함
            }.disposeOnDestroy(this)
    }

    fun onClickUserRules() {
        ActivityNavigator.with(this).rulesInAccount().start()
    }

}