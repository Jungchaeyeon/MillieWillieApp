package com.makeus.milliewillie.ui.dDay

import com.makeus.base.activity.BaseActivity
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityDDayBinding

class DdayActivity: BaseDataBindingActivity<ActivityDDayBinding>(R.layout.activity_d_day) {

    override fun ActivityDDayBinding.onBind() {
        vi = this@DdayActivity
    }


}