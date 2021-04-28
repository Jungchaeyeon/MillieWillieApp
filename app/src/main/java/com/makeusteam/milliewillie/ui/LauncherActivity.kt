package com.makeusteam.milliewillie.ui

import android.os.Handler
import android.os.Looper
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityLauncherBinding

class LauncherActivity: BaseDataBindingActivity<ActivityLauncherBinding>(R.layout.activity_launcher) {
    override fun ActivityLauncherBinding.onBind() {
        vi = this@LauncherActivity

        Handler(Looper.getMainLooper()).postDelayed({
            ActivityNavigator.with(this@LauncherActivity).login().start()
            finish()
        }, 1000)

    }
}