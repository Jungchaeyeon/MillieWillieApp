package com.makeus.milliewillie.ui.holiday

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.view.View
import androidx.lifecycle.MutableLiveData
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
    var minSetDay=MutableLiveData<Int>()

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

    }
    @SuppressLint("CheckResult")
    fun onClickDone(){
    //    "$minSetDay".showShortToastSafe()
            if (allHoliDays.text.toString().toInt()>= minSetDay.value?.toInt() ?: 0) {
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
                Snackbar.make(this.layout_holi_edit_2, "${minSetDay} 일 이상의 총 휴가일수를 입력해주세요.", Snackbar.LENGTH_SHORT).show()
            }

    }

    override fun onResume() {
        super.onResume()
        viewModel.getVacation() {
            if(it){
                when(viewModel.liveHoliType.value){
                    "정기휴가" -> {
                        minSetDay.value = viewModel.regularHoliNum
                        binding.layoutArmyInfo.visibility = View.VISIBLE
                        allHoliDays.setText(viewModel.regularHoliNum.toString())
                    }
                    "포상휴가" -> {
                        minSetDay.value = viewModel.prizeHoliNum
                        binding.layoutArmyInfo.visibility = View.GONE
                        allHoliDays.setText(viewModel.prizeHoliNum.toString())
                    }
                    "기타휴가" ->{
                        minSetDay.value = viewModel.otherHoliNum
                        binding.layoutArmyInfo.visibility = View.GONE
                        allHoliDays.setText(viewModel.otherHoliNum.toString())
                    }
                }}
        else{
            "실패".showShortToastSafe()
        }}

    }
    }
