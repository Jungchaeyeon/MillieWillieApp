package com.makeus.milliewillie.ui.profile

import androidx.fragment.app.Fragment
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoProfileBinding
import com.makeus.milliewillie.util.Log

class ProfileActivity: BaseDataBindingActivity<ActivityInfoProfileBinding>(R.layout.activity_info_profile) {
    var backStack = true
    var fragmentBack: Fragment? = null

    override fun ActivityInfoProfileBinding.onBind() {
        vi = this@ProfileActivity
    }

    fun transitionFragment(fragment: Fragment, kind: String) {
        backStack = true
        fragmentBack = fragment
        when (kind) {
            "add" -> {
                supportFragmentManager.beginTransaction().add(R.id.profile_frame, fragment)
                    .addToBackStack("profile").commitAllowingStateLoss()
            }
            "replace" -> {
                supportFragmentManager.beginTransaction().replace(R.id.profile_frame, fragment)
                    .commitAllowingStateLoss()
            }
        }

    }

    fun onClickEdit() {
        Log.e("onClickEdit")
        transitionFragment(EditProfileFragment(), "add")
    }

    fun onClickBack() {
        onBackPressed()
    }

    override fun onBackPressed() {
        if (backStack) { //상세정보창 프래그먼트를 킨 상태면 뒤로가기했을 때 해당 프래그먼트를 삭제해줌
            fragmentBack?.let { supportFragmentManager.popBackStack() }
            backStack = false
        } else {
            super.onBackPressed()
        }
    }
}