package com.makeus.milliewillie.ui.home.tab4

import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.kakao.sdk.user.UserApiClient
import com.makeus.base.disposeOnDestroy
import com.makeus.base.fragment.BaseDataBindingFragment
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.MyApplication.Companion.globalApplicationContext
import com.makeus.milliewillie.MyApplication.Companion.isLogout
import com.makeus.milliewillie.MyApplication.Companion.loginType
import com.makeus.milliewillie.MyApplication.Companion.userName
import com.makeus.milliewillie.MyApplication.Companion.userProfileImgUrl
import com.makeus.milliewillie.databinding.FragmentInfoBinding
import com.makeus.milliewillie.R
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class InfoFragment : BaseDataBindingFragment<FragmentInfoBinding>(R.layout.fragment_info) {

    private val viewModel by viewModel<InfoViewModel>()
    private val repositoryCached by inject<RepositoryCached>()

    companion object {
        fun getInstance() = InfoFragment()
    }

    override fun onResume() {
        super.onResume()
        exeuteGetUsers()
    }

    override fun FragmentInfoBinding.onBind() {
        vi = this@InfoFragment
        vm = viewModel

        getLoginType()
    }

    private fun exeuteGetUsers() {
        viewModel.apiRepository.getUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.isSuccess) {
                    Log.e("getUsers 호출 성공")

                    userName = it.result.name.toString()
                    userProfileImgUrl = if (it.result.profileImg == null) "" else it.result.profileImg.toString()

                    viewModel.liveDataUserName.postValue(userName)
                } else {
                    Log.e("getUsers 호출 실패")
                    Log.e(it.message)
                }
                Glide.with(binding.infoImgProfile).load(userProfileImgUrl).circleCrop()
                    .placeholder(R.drawable.graphic_profile_big).into(binding.infoImgProfile)
            }.disposeOnDestroy(this)
    }

    fun getLoginType() {
        when (loginType) {
            MyApplication.LOGINTYPE.KAKAO -> {
                binding.infoTextLoginType.text = globalApplicationContext.getString(R.string.kakao_login)
            }
            MyApplication.LOGINTYPE.GOOGLE -> {
                binding.infoTextLoginType.text = globalApplicationContext.getString(R.string.google_login)
            }
        }
    }

    fun onClickLogout() {
        when (loginType) {
            MyApplication.LOGINTYPE.KAKAO -> {
                UserApiClient.instance.me { user, error ->
                    user?.let { UserApiClient.instance.logout {
                        isLogout = true
                        repositoryCached.setValue(LocalKey.INAPP, "T")
                        repositoryCached.setValue(LocalKey.EXERCISEID, (-1))
                        ActivityNavigator.with(this).login().start()
                        activity?.finish()
                    } }
                }
            }
            MyApplication.LOGINTYPE.GOOGLE -> {
                FirebaseAuth.getInstance().signOut()
                isLogout = true
                repositoryCached.setValue(LocalKey.INAPP, "T")
                ActivityNavigator.with(this).login().start()
                activity?.finish()

            }
        }
    }

    fun onClickProfile() {
        ActivityNavigator.with(this).profile().start()
    }

    fun onClickAccount() {
        ActivityNavigator.with(this).account().start()
    }
}
