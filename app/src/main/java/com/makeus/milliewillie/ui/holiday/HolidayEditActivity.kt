package com.makeus.milliewillie.ui.holiday

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.makeus.base.activity.BaseDataBindingActivity
import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.MyApplication
import com.makeus.milliewillie.R
import com.makeus.milliewillie.databinding.ActivityHolidayEditBinding
import com.makeus.milliewillie.ext.showShortToastSafe
import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.ui.SampleToast
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_holiday_edit.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.compat.ScopeCompat.viewModel
import org.koin.android.viewmodel.ext.android.viewModel

class HolidayEditActivity :
    BaseDataBindingActivity<ActivityHolidayEditBinding>(R.layout.activity_holiday_edit) {
    val viewModel by viewModel<HoliViewModel>()
    val repositoryCached by inject<RepositoryCached>()
    var holiType =""

    override fun setupProperties(bundle: Bundle?) {
        super.setupProperties(bundle)
        viewModel.liveHoliType.value =bundle?.getSerializable(ActivityNavigator.KEY_DATA) as String
    }

    override fun ActivityHolidayEditBinding.onBind() {
        vi = this@HolidayEditActivity
        vm = viewModel
        viewModel.bindLifecycle(this@HolidayEditActivity)

        Log.e(viewModel.liveHoliType.value.toString(),"liveHoliType")
        holiType = viewModel.liveHoliType.value.toString()
        when(viewModel.liveHoliType.value){
            "정기휴가" -> {
                layoutArmyInfo.visibility = View.VISIBLE
            }
            "포상휴가" -> {
                layoutArmyInfo.visibility = View.GONE
            }
            "기타휴가" ->{
                layoutArmyInfo.visibility = View.GONE
            }
        }
    }
    @SuppressLint("CheckResult")
    fun onClickDone(){
            if (allHoliDays.text.isNotEmpty()) {
                if(allHoliDays.text.toString().toInt()<36){
                repositoryCached.setValue(LocalKey.PLANTOTOALDAYS, allHoliDays.text.toString())
                viewModel.vacationIdPatch.totalDays = allHoliDays.text.toString().toInt()
                viewModel.vacationIdPatch.useDays = 0

                viewModel.patchVacationId()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe{
                        if(it.isSuccess){
                        ActivityNavigator.with(this).holiday().start()}
                        else{
                            "실패".showShortToastSafe()
                        }
                    }}
                else{
                    Snackbar.make(this.layout_holi_edit_2, "최대 35일까지 가능합니다.", Snackbar.LENGTH_SHORT).show()
                }

            }else{
                Snackbar.make(this.layout_holi_edit_2, "총 휴가일수를 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            }}
    }
