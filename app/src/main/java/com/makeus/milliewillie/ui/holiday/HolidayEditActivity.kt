package com.makeus.milliewillie.ui.holiday

import android.os.Bundle
import android.view.View
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityHolidayEditBinding
import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.util.Log
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HolidayEditActivity :
    BaseDataBindingActivity<ActivityHolidayEditBinding>(R.layout.activity_holiday_edit) {
    val viewModel by viewModel<HoliViewModel>()

    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.liveHoliType.value =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as String
    }

    override fun ActivityHolidayEditBinding.onBind() {
        vi = this@HolidayEditActivity
        vm = viewModel
        viewModel.bindLifecycle(this@HolidayEditActivity)

        Log.e(viewModel.liveHoliType.value.toString(),"liveHoliType")
        when(viewModel.liveHoliType.value){
            "정기휴가" -> {layoutArmyInfo.visibility = View.VISIBLE
                layoutHoliAdd.visibility=View.GONE
            }
            "포상휴가" -> {
                layoutArmyInfo.visibility = View.GONE
                layoutHoliAdd.visibility=View.VISIBLE
            }
            "기타휴가" ->{
                layoutArmyInfo.visibility = View.GONE
                layoutHoliAdd.visibility=View.VISIBLE
            }
        }
    }


}