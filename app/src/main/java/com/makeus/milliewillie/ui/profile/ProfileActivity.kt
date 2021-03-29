package com.makeus.milliewillie.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.MyApplication.Companion.userName
import com.makeus.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoProfileBinding
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileActivity: BaseDataBindingActivity<ActivityInfoProfileBinding>(R.layout.activity_info_profile) {
    var backStack = true
    var fragmentBack: Fragment? = null
    private var userBirthday: String = ""

    private val viewModel by viewModel<ProfileViewModel>()

    companion object {
        const val USER_BIRTHDAY_KEY = "USER_BIRTHDAY_KEY"
    }

    override fun onResume() {
        super.onResume()
        executeGetUsers()
    }

    private fun executeGetUsers() {
        viewModel.apiRepository.getUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("getUsers 호출 성공")
                    Log.e("userName = $userName")

                    userName = it.result.name.toString()
                    userProfileImgUrl = if (it.result.profileImg == null) "" else it.result.profileImg.toString()
                    userBirthday = it.result.birthday.toString()

                    viewModel.liveDataUserBirth.value = userBirthday
                    viewModel.liveDataUserName.postValue(userName)
                    Log.e("userName = $userName")
                    Log.e("userProfileImgUrl = $userProfileImgUrl")
                    Log.e("userBirthday = ${viewModel.liveDataUserBirth.value}")
                } else {
                    Log.e("getUsers 호출 실패")
                    Log.e(it.message)
                }
                Glide.with(binding.userImgUserImage).load(userProfileImgUrl).circleCrop()
                    .placeholder(R.drawable.graphic_profile_big).into(binding.userImgUserImage)
            }.disposeOnDestroy(this)
    }

    override fun ActivityInfoProfileBinding.onBind() {
        vi = this@ProfileActivity
        vm = viewModel
    }

    fun transitionFragment(fragment: Fragment, kind: String) {
        backStack = true
        fragmentBack = fragment
        when (kind) {
            "add" -> {
                supportFragmentManager.beginTransaction().add(R.id.profile_frame, fragment.apply {
                    arguments = Bundle().apply {
                        putString(USER_BIRTHDAY_KEY, userBirthday)
                        Log.e("userBirthday In Profile = $userBirthday")
                    }
                })
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