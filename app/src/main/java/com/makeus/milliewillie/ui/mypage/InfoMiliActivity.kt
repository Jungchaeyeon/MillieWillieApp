package com.makeus.milliewillie.ui.mypage

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoMiliBinding
import com.makeus.milliewillie.ui.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class InfoMiliActivity :
    BaseDataBindingActivity<ActivityInfoMiliBinding>(R.layout.activity_info_mili) {

    val viewModel by viewModel<MyPageEditViewModel>()

    companion object {
        fun getInstance() = InfoMiliActivity()
    }

    override fun ActivityInfoMiliBinding.onBind() {
        vi = this@InfoMiliActivity
        vm = viewModel
        viewModel.bindLifecycle(this@InfoMiliActivity)

        btnX.setPadding(10,10,10,10)
    }
    fun onClickEdit(){
        ActivityNavigator.with(this).infoenlist().start()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getUsers()
        viewModel.toMonthPromPercent.value= HobongPercent().toString()

    }
    fun HobongPercent(): Float {
        val cal = Calendar.getInstance()
        val df = SimpleDateFormat("yyyy-MM-dd")
        val allMonthDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        val today = allMonthDay- Calendar.DATE

        val allDays = allMonthDay // 전체 날
        val nowDays = allMonthDay - today// 첫날 ~ 오늘

        return (nowDays*100.toFloat()/allDays.toFloat())
    }

}