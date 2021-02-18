package com.makeus.milliewillie.ui

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMainBinding

class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun ActivityMainBinding.onBind() {
        vi = this@MainActivity
    }
}