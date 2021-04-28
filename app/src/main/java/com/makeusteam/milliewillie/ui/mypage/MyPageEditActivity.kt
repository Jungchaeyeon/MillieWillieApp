package com.makeusteam.milliewillie.ui.mypage

import android.view.View
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityMyPageEditBinding
import com.makeusteam.milliewillie.ui.intro.UserViewModel
import com.makeusteam.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_intro_setting_name.*
import kotlinx.android.synthetic.main.activity_my_page_edit.*
import kotlinx.android.synthetic.main.fragment_mypage_edit.*
import org.koin.android.viewmodel.ext.android.viewModel

class MyPageEditActivity :
    BaseDataBindingActivity<ActivityMyPageEditBinding>(R.layout.activity_my_page_edit) {

    val viewModel by viewModel<UserViewModel>()

    override fun ActivityMyPageEditBinding.onBind() {
        vi = this@MyPageEditActivity
        vm = viewModel
        viewModel.bindLifecycle(this@MyPageEditActivity)

    }


    fun onClickDate(position: Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)
            }.show(supportFragmentManager)
    }

    fun onClickType() {

    }

    fun onClickImg() {

    }

    fun onClickName() {
        viewModel.liveModifyTitle.postValue("이름")
        viewModel.liveEditData.postValue(viewModel.liveUserName.value)
        toModifyPage()

    }

    fun onClickBirth() {
        viewModel.liveModifyTitle.postValue("생년월일")
        viewModel.liveEditData.postValue(viewModel.liveUserBirth.value)
        MyPageBirthFragment.getInstance()
            .setOnClickOk {
                viewModel.liveUserBirth.postValue(it)
            }.show(supportFragmentManager)

    }

    fun onClickGoal() {
        viewModel.liveModifyTitle.postValue("목표")
        viewModel.liveEditData.postValue(viewModel.liveUserGoal.value)

        toModifyPage()
    }

    fun toModifyPage() {
        mypage_edit_container.visibility = View.VISIBLE
        supportFragmentManager?.beginTransaction()
            ?.attach(MyPageEditFragment.getInstance())
            ?.replace(R.id.mypage_edit_container, MyPageEditFragment.getInstance())
            ?.addToBackStack(null)?.commit()
    }
}