package com.makeus.milliewillie.ui.profile

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.base.disposeOnDestroy
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.MyApplication.Companion.userName
import com.makeus.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityInfoProfileBinding
import com.makeus.milliewillie.model.PatchUsersRequest
import com.makeus.milliewillie.ui.fragment.DatePickekProfileBirthDayBottomSheetDialogFragment
import com.makeus.milliewillie.util.BasicTextFormat
import com.makeus.milliewillie.util.Loading
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileActivity: BaseDataBindingActivity<ActivityInfoProfileBinding>(R.layout.activity_info_profile) {
    var backStack = true
    var fragmentBack: Fragment? = null

    private val viewModel by viewModel<ProfileViewModel>()

    companion object {
        const val USER_BIRTHDAY_KEY = "USER_BIRTHDAY_KEY"
        var userBirthday: String? = null
    }

    override fun onResume() {
        super.onResume()
        Glide.with(binding.userImgUserImage).load(userProfileImgUrl).circleCrop()
            .placeholder(R.drawable.graphic_profile_big).into(binding.userImgUserImage)
    }

    override fun ActivityInfoProfileBinding.onBind() {
        vi = this@ProfileActivity
        vm = viewModel
        onBackPressed()
        executeGetUsers()

    }

    private fun executeGetUsers() {
        viewModel.apiRepository.getUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("getUsers 호출 성공")

                    userName = it.result.name.toString()
                    userProfileImgUrl = if (it.result.profileImg == null) "" else it.result.profileImg.toString()
                    userBirthday = if (it.result.birthday.isNullOrBlank()) "" else it.result.birthday.toString()

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

    private fun executePatchUsers() {
        if (userBirthday.isNullOrBlank()
            || userBirthday == "생년월일 입력") userBirthday = null

//        val profileImg = if (downloadUri == null) null
//        else downloadUri.toString()

        viewModel.apiRepository.patchUsers(
            body = PatchUsersRequest(
                name = binding.userEditUserName.text.toString(),
                profileImg = userProfileImgUrl, birthday = userBirthday
            )
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess){
                    Log.e("patchUsers 호출 성공")

                    userName = it.result.name
                    userProfileImgUrl = it.result.profileImg
                    userBirthday = it.result.birthday
                    viewModel.liveDataUserBirth.postValue(userBirthday)
                } else {
                    Log.e("patchUsers 호출 실패")
                    Log.e(it.message)
                }
                Loading.dissmiss()
            }.disposeOnDestroy(this)
    }


    fun transitionFragment(fragment: Fragment, kind: String) {
        backStack = true
        fragmentBack = fragment
        when (kind) {
            "add" -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.profile_frame, fragment.apply {
                    arguments = Bundle().apply {
                        putString(USER_BIRTHDAY_KEY, userBirthday)
                        Log.e("userBirthday In Profile = $userBirthday")
                    }
                })
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
            "replace" -> {
                supportFragmentManager.beginTransaction().replace(R.id.profile_frame, fragment)
                    .commitAllowingStateLoss()
            }
        }

    }

    fun onClickPhoto() {
//        transitionFragment(PhotoSelectFragment(), "add")
        ActivityNavigator.with(this).photoSelect().start()
    }

    private var isEdit = false

    fun onClickEdit() {
        isEdit = !isEdit
        when (isEdit) {
            true -> {
                if (userBirthday == null || userBirthday == "") userBirthday = "생년월일 입력"
                viewModel.liveDataUserBirth.value = userBirthday
                binding.userTextEdit.setText(R.string.complete)
                binding.userImgBirthday.visibility = View.VISIBLE
                binding.userImgUserName.visibility = View.VISIBLE
                binding.userEditUserName.isEnabled = true

                binding.userImgUserImage.setOnClickListener {
                    onClickPhoto()
                }
            }
            false -> {
                binding.userTextEdit.setText(R.string.edit)
                binding.userImgBirthday.visibility = View.GONE
                binding.userImgUserName.visibility = View.GONE
                binding.userEditUserName.isEnabled = false

                executePatchUsers()
            }
        }

    }

    fun onClickBirthday() {
        DatePickekProfileBirthDayBottomSheetDialogFragment.getInstance()
            .setOnClickOk{dateYear, dateMonth, dateDay ->
                userBirthday = BasicTextFormat.BasicDateFormat(dateYear, dateMonth, dateDay)
                viewModel.liveDataUserBirth.value = userBirthday
            }.show(supportFragmentManager)
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