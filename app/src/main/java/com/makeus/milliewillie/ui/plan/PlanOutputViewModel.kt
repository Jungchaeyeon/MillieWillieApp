package com.makeus.milliewillie.ui.plan

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.makeus.base.disposeOnDestroy
import com.makeus.base.viewmodel.BaseViewModel
import com.makeus.milliewillie.model.PlanDiary
import com.makeus.milliewillie.model.PlanDiaryRequest
import com.makeus.milliewillie.model.PlansGet
import com.makeus.milliewillie.model.PlansRequest
import com.makeus.milliewillie.repository.ApiRepository
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PlanOutputViewModel(
    val repositoryCached: RepositoryCached,
    val apiRepository: ApiRepository
) : BaseViewModel() {

    var planDiaryRequest = PlanDiaryRequest()

    var dayInfo = MutableLiveData<String>()
    var planType =MutableLiveData<String>()
    var title =MutableLiveData<String>()
    var plansGet = PlansGet.Result()
    val liveDayNNight =MutableLiveData<String>()
    val liveContent = MutableLiveData<String>().apply { value = "" }
    var todayFormat : String=""
    val df = SimpleDateFormat("yyyy-MM-dd")


    // TodoItem list
    val livePlanTodoList = MutableLiveData<ArrayList<PlansGet.Result.Work>>()
    var planTodos = ArrayList<PlansGet.Result.Work>()

    //TodoMethod
    fun addTodo(item: PlansGet.Result.Work) {
        planTodos.add(item)
        livePlanTodoList.value = planTodos
    }
    fun addTodoAll(item : List<PlansGet.Result.Work>){
        planTodos.addAll(item)
        livePlanTodoList.value = planTodos
        Log.e(planTodos.toString(),"planTodos")
    }
    fun replaceTodo() {
        planTodos.clear()
        livePlanTodoList.value = planTodos

    }
    val liveMemoList = MutableLiveData<ArrayList<PlansGet.Result.Diary>>()
    var memoLists = ArrayList<PlansGet.Result.Diary>()

    // MemoList Method
    fun addMemoAll(item : List<PlansGet.Result.Diary>){
        memoLists.addAll(item)
        liveMemoList.value = memoLists
        Log.e(memoLists.toString(),"planTodos")
    }

    init {

        initToday()
    }

    //일정 Patch
    fun patchPlanDiary() = apiRepository.patchPlanDiary(
        PlanDiaryRequest(
            content = liveContent.value.toString()
        ),
        path = repositoryCached.getDiaryId().toLong()
    ).subscribe(
        {
            if(it.isSuccess){
                Log.e("일정등록 성공")
                // set plan Key
                repositoryCached.setValue(LocalKey.DIARYID, it.result.diaryId)
            }
            else{
                Log.e("일정등록 실패")
            }
        },{})

    //일정 조회
    fun getPlans(response: (Boolean) -> Unit) = apiRepository.getPlans(
        path = repositoryCached.getPlanId().toLong()
    ) .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
        {
            memoLists.clear()
            if(it.isSuccess){
                Log.e("일정조회 성공")
                // set plan Key
                plansGet = it.result
                Log.e(plansGet.toString(),"plansGet")
                dayInfo.value = plansGet.dateInfo
                planType.value = plansGet.planType
                title.value = plansGet.title
                calDayNNight(plansGet.startDate, plansGet.endDate)
                addTodoAll(it.result.work)
                addMemoAll(it.result.diary)
                repositoryCached.setValue(LocalKey.PLANID, it.result.planId.toString())
                response.invoke(true)

            }
            else{
                Log.e("일정조회 실패")
                response.invoke(false)
            }
        },{}).disposeOnDestroy(this)

    fun deletePlans() = apiRepository.deletePlans(path = repositoryCached.getPlanId().toLong()).subscribe(
        {
            if(it.isSuccess){
                Log.e("일정삭제 성공")
                // set plan Key
               // repositoryCached.setValue(LocalKey.PLANID, it.result.planId)
            }
            else{
                Log.e("일정삭제 실패")
            }
        },{}
    ).disposeOnDestroy(this)

    // 할일 TF patch
    fun patchDiary() = apiRepository.patchDiary(path = repositoryCached.getWorkId().toLong())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            if(it.isSuccess){
                Log.e("할일 T/F 성공")
                // set work Key
                repositoryCached.setValue(LocalKey.WORKID, it.result.workId)
            }
            else{
                Log.e("할일 T/F 실패")
            }
        },{})


    //몇박 몇일
    fun calDayNNight(startDate: String, endDate : String){
        val startDate = df.parse(startDate)
        val endDate = df.parse(endDate)
        val calcuDate = (endDate.time - startDate.time) / (60 * 60 * 24 * 1000)

        liveDayNNight.value =  "$calcuDate" + "박${calcuDate + 1}일"
        Log.e(liveDayNNight.value.toString(),"liveDayNNight")
    }
    fun initToday(){
        val cal = Calendar.getInstance().time
        val dayDf = SimpleDateFormat("MM월 dd일")
        todayFormat = dayDf.format(cal).toString()
        Log.e(todayFormat.toString(),"planTodos")
    }
}