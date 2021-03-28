package com.makeus.milliewillie.repository


import com.makeus.milliewillie.model.*

import com.makeus.milliewillie.model.PlansRequest

import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.network.api.Api
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import io.reactivex.android.schedulers.AndroidSchedulers

class ApiRepository(
    private val repositoryCached: RepositoryCached,
    private val apiTest: Api
) {
    // observeOn() , subscribeOn() 차이
    // fun gallerySearchList(keyword : String) = apiTest.gallerySearchList(keyword = keyword).observeOn(AndroidSchedulers.mainThread())


    fun kakaoLogin() = apiTest.kakaoLogin().doOnNext {
            if (!it.result.isMember) {
                 repositoryCached.setValue(LocalKey.ISMEMBER, false)
            } else {
                repositoryCached.setValue(LocalKey.ISMEMBER, true)
            }
    }.switchMap {
        //jwt API 호출
        apiTest.jwt()
    }

    fun jwt() = apiTest.jwt().doOnNext {}
    fun getUsers() = apiTest.getUsers()
    fun patchUsers(usersPatch : UsersPatch) = apiTest.patchUsers(usersPatch)
    fun users(usersRequest : UsersRequest) = apiTest.users(usersRequest).observeOn(AndroidSchedulers.mainThread())
    fun patchVacationId(vacationIdRequest: VacationIdPatch, path: Long) = apiTest.patchVacationId(vacationIdRequest,path).observeOn(AndroidSchedulers.mainThread())
    fun getVacation() = apiTest.getVacation()
    fun patchPlanDiary(planDiaryRequest: PlanDiaryRequest, path: Long) = apiTest.patchPlanDiary(body = planDiaryRequest, diaryId = path)
    fun getPlans(path: Long) = apiTest.getPlans(planId = path)
    fun deletePlans(path: Long) = apiTest.getPlans(planId = path)
    fun postEmotionsRecord(emotionsRecordRequest: EmotionsRecordRequest) = apiTest.postEmotionsRecord(emotionsRecordRequest)
    fun patchEmotionsRecord(emotionsRecordRequest: EmotionsRecordRequest, path : Long) = apiTest.patchEmotionsRecord(body = emotionsRecordRequest, emotionsRecordId = path)

    //fun users(name : String) = apiTest.users(name)
    fun schedule(body: ScheduleRequest) = apiTest.schedule(body)
    fun getDailyWeight(path: Long) = apiTest.getDailyWeight(path)
    fun getWeightRecord(path: Long, viewMonth:Int, viewYear: Int) = apiTest.getWeightRecord(exerciseId =  path, viewMonth = viewMonth, viewYear = viewYear)
    fun getAllRoutines(path: Long) = apiTest.getAllRoutines(exerciseId = path)
    fun getRoutines(path: Long, targetDate: String) = apiTest.getRoutines(exerciseId = path, targetDate = targetDate)
    fun postFirstWeight(body: FirstWeightRequest) = apiTest.postFirstWeight(body)
    fun postDailyWeight(body: PostDailyWeightRequest, path: Long) = apiTest.postDailyWeight(body, path)
    fun postRoutine(body: PostRoutineRequest, path: Long) = apiTest.postRoutine(body = body, exerciseId = path)
    fun patchGoalWeight(body: PatchGoalWeightRequest, path: Long) = apiTest.patchGoalWeight(body = body, exerciseId = path)
    fun patchTodayWeight(path: Long, body: PatchTodayWeightRequest) = apiTest.patchTodayWeight(exerciseId = path, body = body)
    fun deleteRoutine(path: Long) = apiTest.deleteRoutine(exerciseId = path)
    fun plans(plansRequest: PlansRequest) = apiTest.plans(plansRequest).doOnNext {
    }.observeOn(AndroidSchedulers.mainThread())
    //회원가입

}