package com.makeus.milliewillie.ui.mypage

import android.os.Bundle
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoMiliBinding
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.MainViewModel
import com.makeus.milliewillie.util.Log
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
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

    override fun onResume() {
        super.onResume()
       // viewModel.getUsers()
        Log.e(allDdayPercent.toString(),"nextDayPercent")
        Log.e(nextDdayPercent.toString(),"nextDayPercent")
        Log.e(mothPromDday.toString(),"nextDayPercent")
    }

}