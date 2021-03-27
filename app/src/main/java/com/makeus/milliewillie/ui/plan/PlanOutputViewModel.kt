package com.makeus.milliewillie.ui.plan

import androidx.lifecycle.MutableLiveData
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.PlanDiaryRequest
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log

class PlanOutputViewModel(
    val repositoryCached: RepositoryCached,
    val apiRepository: ApiRepository
) : BaseViewModel() {

    var planDiaryRequest = PlanDiaryRequest()
    val liveContent = MutableLiveData<String>().apply { value = "" }

    init {
        repositoryCached.setValue(LocalKey.PLANID,"35")
        getPlans()
    }

    //일정 Patch
    fun patchPlanDiary() = apiRepository.patchPlanDiary(
        PlanDiaryRequest(
            content = liveContent.value.toString()
        ),
        path = 0L
    ).subscribe(
        {
            if(it.isSuccess){
                Log.e("일정등록 성공")
                repositoryCached.setValue(LocalKey.DIARYID, it.result.diaryId)
            }
            else{
                Log.e("일정등록 실패")
            }
        },{})
    //일정 조회
    fun getPlans() = apiRepository.getPlans(
        path = 0L
    ).subscribe(
        {
            if(it.isSuccess){
                Log.e("일정조회 성공")
                repositoryCached.setValue(LocalKey.PLANID, it.result.planId)
            }
            else{
                Log.e("일정조회 실패")
            }
        },{})
    fun deletePlans() = apiRepository.deletePlans(path = "35".toInt().toLong()).subscribe(
        {
            if(it.isSuccess){
                Log.e("일정삭제 성공")
                repositoryCached.setValue(LocalKey.PLANID, it.result.planId)
            }
            else{
                Log.e("일정삭제 실패")
            }
        },{}
    )
}