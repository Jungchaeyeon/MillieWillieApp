package com.makeusteam.milliewillie.ui.mypage

import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityInfoMiliBinding
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class InfoMiliActivity :
    BaseDataBindingActivity<ActivityInfoMiliBinding>(R.layout.activity_info_mili) {

    val viewModel by viewModel<MyPageEditViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    var allDdayPercent =repositoryCached.getDday()
    var nextDdayPercent = repositoryCached.getNextPromDday()
    var mothPromDday = repositoryCached.getMonthPromDday()
    companion object {
        fun getInstance() = InfoMiliActivity()
    }


    override fun ActivityInfoMiliBinding.onBind() {
        vi = this@InfoMiliActivity
        vm = viewModel
        viewModel.bindLifecycle(this@InfoMiliActivity)
        nextDdayPercent=repositoryCached.getNextPromDday()

        btnX.setPadding(10,10,10,10)
    }
    fun onClickEdit(){
        ActivityNavigator.with(this).infoenlist().start()
    }
    fun onClickBack(){
        onBackPressed()
    }

    override fun onBackPressed() {
        ActivityNavigator.with(this).main().start()
    }
    override fun onResume() {
        super.onResume()
        viewModel.getUsers()
        Log.e(allDdayPercent.toString(),"nextDayPercent")
        Log.e(nextDdayPercent.toString(),"nextDayPercent")
        Log.e(mothPromDday.toString(),"nextDayPercent")
    }

}