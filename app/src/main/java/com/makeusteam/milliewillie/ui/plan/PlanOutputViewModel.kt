package com.makeusteam.milliewillie.ui.plan

import androidx.lifecycle.MutableLiveData
import com.makeusteam.base.disposeOnDestroy
import com.makeusteam.base.viewmodel.BaseViewModel
import com.makeusteam.milliewillie.model.*
import com.makeusteam.milliewillie.repository.ApiRepository
import com.makeusteam.milliewillie.repository.local.LocalKey
import com.makeusteam.milliewillie.repository.local.RepositoryCached
import com.makeusteam.milliewillie.util.Log
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


    //?????? ????????????Patch
    fun patchPlanDiary() = apiRepository.patchPlanDiary(
        PlanDiaryRequest(
            content = liveContent.value.toString()
        ),
        path = repositoryCached.getDiaryId().toLong()
    ).subscribe(
        {
            if(it.isSuccess){
                Log.e("???????????? ?????? ??????")
                // set plan Key
                repositoryCached.setValue(LocalKey.DIARYID, it.result.diaryId)
            }
            else{
                Log.e("???????????? ?????? ??????")
            }
        },{}).disposeOnDestroy(this)

    //?????? ??????
    fun getPlans(response: (Boolean) -> Unit) = apiRepository.getPlans(
        path = repositoryCached.getPlanId().toLong()
    ) .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
        {
            memoLists.clear()
            if(it.isSuccess){
                Log.e("???????????? ??????")
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
                Log.e("???????????? ??????")
                response.invoke(false)
            }
        },{}).disposeOnDestroy(this)


    fun deletePlans() = apiRepository.deletePlans(path = repositoryCached.getPlanId().toLong()).subscribe(
        {
            if(it.isSuccess){
                Log.e("???????????? ??????")
                // set plan Key
               // repositoryCached.setValue(LocalKey.PLANID, it.result.planId)
            }
            else{
                Log.e("???????????? ??????")
            }
        },{}
    ).disposeOnDestroy(this)

    // ?????? TF patch
    fun patchDiary() = apiRepository.patchDiary(path = repositoryCached.getWorkId().toLong())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe({
            if(it.isSuccess){
                Log.e("?????? T/F ??????")
                // set work Key
                repositoryCached.setValue(LocalKey.WORKID, it.result.workId)
            }
            else{
                Log.e("?????? T/F ??????")
            }
        },{}).disposeOnDestroy(this)


    //?????? ??????
    fun calDayNNight(startDate: String, endDate : String){
        val startDate = df.parse(startDate)
        val endDate = df.parse(endDate)
        val calcuDate = (endDate.time - startDate.time) / (60 * 60 * 24 * 1000)

        liveDayNNight.value =  "$calcuDate" + "???${calcuDate + 1}???"
        Log.e(liveDayNNight.value.toString(),"liveDayNNight")
    }
    fun initToday(){
        val cal = Calendar.getInstance().time
        val dayDf = SimpleDateFormat("MM??? dd???")
        todayFormat = dayDf.format(cal).toString()
        Log.e(todayFormat.toString(),"planTodos")
    }
}