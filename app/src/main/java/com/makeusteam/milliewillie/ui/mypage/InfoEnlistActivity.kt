package com.makeusteam.milliewillie.ui.mypage

import android.annotation.SuppressLint
import com.google.android.material.snackbar.Snackbar
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityInfoEnlistBinding
import com.makeusteam.milliewillie.ui.SampleToast
import com.makeusteam.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import com.makeusteam.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_info_enlist.*
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import org.koin.android.viewmodel.ext.android.viewModel

class InfoEnlistActivity :
    BaseDataBindingActivity<ActivityInfoEnlistBinding>(R.layout.activity_info_enlist) {

    val viewModel by viewModel<MyPageEditViewModel>()

    @SuppressLint("ClickableViewAccessibility")
    override fun ActivityInfoEnlistBinding.onBind() {
        vi = this@InfoEnlistActivity
        vm = viewModel
        viewModel.bindLifecycle(this@InfoEnlistActivity)

        binding.layoutInfo.setOnTouchListener { p0, p1 ->
            binding.layoutInfo.requestFocus()
            hideKeyboard()
            binding.btnGoal.clearFocus()
            true
        }

    }


    fun onClickDate(position: Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)
                when (position) {
                    0 -> viewModel.usersResponse.startDate = viewModel.dateChangeTest(it)
                    1 -> viewModel.usersResponse.endDate = viewModel.dateChangeTest(it)
                    2 -> viewModel.usersResponse.strPrivate = viewModel.dateChangeTest(it)
                    3 -> viewModel.usersResponse.strCorporal = viewModel.dateChangeTest(it)
                    4 -> viewModel.usersResponse.strSergeant = viewModel.dateChangeTest(it)
                }
            }.show(supportFragmentManager)
    }

    fun onClickType() {
        ServiceTypeBottomSheetDialogFragment.getInstance()
            .setOnClickType {
                viewModel.liveServicetype.postValue(it)
                viewModel.usersPatch.serveType = it
            }
            .show(supportFragmentManager)
    }

    fun onClickDone() {
        if (!viewModel.enlistValueTest()) {
            Snackbar.make(this.layout_info, "날짜 정보를 확인해주세요.", Snackbar.LENGTH_SHORT).show()
        } else {
            viewModel.liveGoal.value = btn_goal.text.toString()
            viewModel.usersPatch.goal = btn_goal.text.toString()
            Log.e(btn_goal.text.toString(),"목표text")
            Log.e(viewModel.usersPatch.goal,"목표text Patch")
            Log.e(viewModel.liveGoal.value.toString(),"목표text LiveData")

            patchGoal()
            patchUsers()
        }
        ActivityNavigator.with(this).infomili().start()
    }
    fun patchGoal() =
        viewModel.patchGoal()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                nextStep(it.isSuccess)
            }, {})

    fun patchUsers() =
        viewModel.patchUsers()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
               nextStep(it.isSuccess)
            }, {})

    fun nextStep(it : Boolean){
        if (it) {
            SampleToast.createToast(this, "수정이 완료되었습니다.")?.show()
            ActivityNavigator.with(this).infomili().start()
        } else {
            SampleToast.createToast(this,"수정실패")?.show()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.btnGoal.isFocusableInTouchMode
    }
}