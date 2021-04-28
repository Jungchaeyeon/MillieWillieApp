package com.makeusteam.milliewillie.ui.intro

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import com.makeusteam.base.activity.BaseDataBindingActivity
import com.makeusteam.milliewillie.ActivityNavigator
import com.makeusteam.milliewillie.R
import com.makeusteam.milliewillie.databinding.ActivityIntroEnlistDateSergeantBinding
import com.makeusteam.milliewillie.model.UsersRequest
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.ui.plan.DatePickerBasicBottomSheetDialogFragment
import com.makeusteam.milliewillie.util.Log
import kotlinx.android.synthetic.main.activity_intro_enlist_date_sergeant.*
import kotlinx.android.synthetic.main.activity_make_plan.*
import kotlinx.android.synthetic.main.datepicker_bottom_sheet_basic.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class IntroEnlistDateSergeantActivity :
    BaseDataBindingActivity<ActivityIntroEnlistDateSergeantBinding>(R.layout.activity_intro_enlist_date_sergeant) {

    private val repositoryCached by inject<RepositoryCached>()
    val viewModel by viewModel<UserViewModel>()

    override fun ActivityIntroEnlistDateSergeantBinding.onBind() {
        vi = this@IntroEnlistDateSergeantActivity
        vm = viewModel
        viewModel.bindLifecycle(this@IntroEnlistDateSergeantActivity)


        viewModel.usersRequest.startDate = dateformat(viewModel.today())
        viewModel.liveDateButtonList[1].value = ""
        viewModel.liveDateButtonList[2].value = ""
    }

    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.usersRequest = bundle?.getSerializable(ActivityNavigator.KEY_DATA) as UsersRequest
    }

    fun onClickDate(position: Int) {
        DatePickerBasicBottomSheetDialogFragment.getInstance()
            .setOnClickOk {
                when (position) {
                    0 -> viewModel.usersRequest.startDate = dateformat(it)
                    1 -> viewModel.usersRequest.endDate = dateformat(it)
                    2 -> viewModel.usersRequest.proDate = dateformat(it)
                }
                viewModel.liveDateButtonList[position].postValue(it)
            }.show(supportFragmentManager)
    }

    fun onClickDone() {
            viewModel.sergeantNull()
        if (btn_discharge.text.isEmpty() || btn_promotion.text.isEmpty()) {
            Snackbar.make(this.layout_sergeant, "날짜를 선택해주세요", Snackbar.LENGTH_LONG).show();
        }
        else {
            if (viewModel.enlistValueTestSergeant()) {
                ActivityNavigator.with(this).goal(viewModel.usersRequest).start()
                repositoryCached.setValue(LocalKey.ENDDATE, viewModel.liveDateButtonList[1])
                repositoryCached.setValue(
                    LocalKey.ENDDDAY,
                    viewModel.calDday(repositoryCached.getEnd())
                )
            }
            else{
                Snackbar.make(this.layout_sergeant, "날짜정보가 옳바르지 않습니다.", Snackbar.LENGTH_SHORT).show();
            }
        }

    }

    fun dateformat(date: String): String {
        val df = SimpleDateFormat("yyyy.MM.dd (EE)")
        val dateformat = SimpleDateFormat("yyyy-MM-dd")
        val resultDate = df.parse(date)
        Log.e(resultDate.toString(), "parse")
        return dateformat.format(resultDate)
    }

    override fun onResume() {
        super.onResume()

            viewModel.liveServiceId.postValue(2)
    }


}