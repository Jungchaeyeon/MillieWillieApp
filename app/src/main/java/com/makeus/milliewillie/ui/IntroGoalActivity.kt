package com.makeus.milliewillie.ui

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityIntroGoalBinding
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import kotlinx.android.synthetic.main.activity_intro_goal.*
import org.koin.android.ext.android.inject

class IntroGoalActivity :
    BaseDataBindingActivity<ActivityIntroGoalBinding>(R.layout.activity_intro_goal) {

    private val repositoryCached by inject<RepositoryCached>()

    override fun ActivityIntroGoalBinding.onBind() {
        vi = this@IntroGoalActivity


    }

    fun onClickBack() {
        onBackPressed()
    }

    fun onClickDone() {
        if (edt_goal.text.isNotEmpty()) {
            repositoryCached.setValue(LocalKey.GOAL, edt_goal.text.toString())
            ActivityNavigator.with(this).main().start()
        } else {
            "목표를 입력해 주세요!"
        }
    }
}