package com.makeusteam.milliewillie.ui.info

import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.MyApplication.Companion.globalApplicationContext
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityInfoAccountBinding
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.SampleToast
import com.makeusteam.milliewillie.util.Log
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
                repositoryCached.setValue(LocalKey.TOKEN, "")
                repositoryCached.setValue(LocalKey.ISMEMBER, false)
                repositoryCached.setValue(LocalKey.ISINPUTWEIGHT, false)
                repositoryCached.setValue(LocalKey.ISINPUTGOAL, false)
                repositoryCached.setValue(LocalKey.EXERCISEID, (-1))
                repositoryCached.setValue(LocalKey.POSTYEAR, 10000)
                repositoryCached.setValue(LocalKey.POSTMONTH, 100)
                repositoryCached.setValue(LocalKey.POSTDAY, 100)
                SampleToast.createToast(this, globalApplicationContext.getString(R.string.toast_withdraw))?.show()
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