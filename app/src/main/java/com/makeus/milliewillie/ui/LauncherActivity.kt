package com.makeus.milliewillie.ui

import android.os.Handler
import android.os.Looper
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityLauncherBinding

class LauncherActivity: BaseDataBindingActivity<ActivityLauncherBinding>(R.layout.activity_launcher) {
    override fun ActivityLauncherBinding.onBind() {
        vi = this@LauncherActivity

        Handler(Looper.getMainLooper()).postDelayed({
            ActivityNavigator.with(this@LauncherActivity).login().start()
            finish()
        }, 1000)

    }
}