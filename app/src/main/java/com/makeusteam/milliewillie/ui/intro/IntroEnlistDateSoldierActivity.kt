package com.makeusteam.milliewillie.ui.intro

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import  com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityIntroEnlistDateSoldierBinding
import com.makeusteam.milliewillie.model.UsersRequest
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import kotlinx.android.synthetic.main.activity_intro_enlist_date_soldier.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_basic.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class IntroEnlistDateSoldierActivity :
    BaseDataBindingActivity<ActivityIntroEnlistDateSoldierBinding>(R.layout.activity_intro_enlist_date_soldier) {

    private val viewModel by viewModel<UserViewModel>()
    val repositoryCached by inject<RepositoryCached>()

    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.usersRequest =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as UsersRequest
    }

    override fun ActivityIntroEnlistDateSoldierBinding.onBind() {
        vi = this@IntroEnlistDateSoldierActivity
        vm = viewModel
        viewModel.bindLifecycle(this@IntroEnlistDateSoldierActivity)


    }

    fun onClickDate(position: Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                viewModel.liveDateButtonList[position].postValue(it)

                when(position){
                    1-> viewModel.usersRequest.endDate = viewModel.dateChangeTest(it)
                    2-> viewModel.usersRequest.strPrivate = viewModel.dateChangeTest(it)
                    3-> viewModel.usersRequest.strCorporal = viewModel.dateChangeTest(it)
                    4 -> viewModel.usersRequest.strSergeant = viewModel.dateChangeTest(it)
                }

                if(position == 0) {
                    viewModel.usersRequest.startDate=viewModel.dateChangeTest(it)
                    viewModel.calculateDay(it)
                }
            }.show(supportFragmentManager)
    }

    fun onClickDone() {
        if (viewModel.enlistValueTest()) {
                viewModel.calDday(btn_discharge.text.toString()).toString()
            ActivityNavigator.with(this).goal(viewModel.usersRequest).start()
        }
        else{
            Snackbar.make(this.layout_enlistSet,"?????? ????????? ???????????? ????????????.",Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.enlistDataInit()
        viewModel.usersRequest
    }





}