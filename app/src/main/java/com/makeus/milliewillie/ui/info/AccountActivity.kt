package com.makeus.milliewillie.ui.info

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoAccountBinding
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class AccountActivity: BaseDataBindingActivity<ActivityInfoAccountBinding>(R.layout.activity_info_account) {

    private val viewModel by viewModel<AccountViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

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
                repositoryCached.setValue(LocalKey.ISINPUTGOAL, false)
                repositoryCached.setValue(LocalKey.TOKEN, "")
                repositoryCached.setValue(LocalKey.ISMEMBER, false)
                SampleToast.createToast(this, getString(R.string.toast_withdraw))?.show()
                ActivityNavigator.with(this).login().start()
            }.disposeOnDestroy(this)
    }

    fun onClickUserRules() {
        ActivityNavigator.with(this).rulesInAccount().start()
    }

    fun onClickBack() {
        onBackPressed()
    }

}