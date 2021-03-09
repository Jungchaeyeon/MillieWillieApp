package com.makeus.milliewillie.ui

import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.fragment.BaseDataBindingBottomSheetFragment
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityMainBinding
import com.makeus.milliewillie.ui.common.BasicBottomSheetDialogFragment
import com.makeus.milliewillie.ui.common.BasicBottomSheetDialogFragment.Companion.getInstance

class MainActivity : BaseDataBindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    override fun ActivityMainBinding.onBind() {
        vi = this@MainActivity

        val customDialog = BasicBottomSheetDialogFragment.getInstance()
        customDialog.show(supportFragmentManager, "custom_dialog")
    }
}