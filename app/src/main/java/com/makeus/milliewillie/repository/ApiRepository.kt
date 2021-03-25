package com.makeus.milliewillie.repository


import com.makeus.milliewillie.model.*

import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.model.KakaoRequest
import com.makeus.milliewillie.model.PlansRequest

import com.makeus.milliewillie.model.UsersRequest
import com.makeus.milliewillie.network.api.Api
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached
import com.makeus.milliewillie.util.Log
import io.reactivex.android.schedulers.AndroidSchedulers

class ApiRepository(
    private val repositoryCached: RepositoryCached,
    private val apiTest: Api
) {
    // observeOn() , subscribeOn() 차이
    // fun gallerySearchList(keyword : String) = apiTest.gallerySearchList(keyword = keyword).observeOn(AndroidSchedulers.mainThread())


    fun kakaoLogin() = apiTest.kakaoLogin().doOnNext {

            if (!it.result.isMember) {
                Log.e(it.result.isMember.toString(),"가입되지 않은 멤버")
                 repositoryCached.setValue(LocalKey.ISMEMBER, false)
            } else {
                Log.e(it.result.isMember.toString(),"가입된 멤버")
                repositoryCached.setValue(LocalKey.ISMEMBER, true)
                repositoryCached.setValue(LocalKey.TOKEN, it.result.jwt)
            }
    }.switchMap {
        //jwt API 호출
        apiTest.jwt()
    }

    fun jwt() = apiTest.jwt().doOnNext {}

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
        Log.e(it.result.color.toString(),"왜 안돼?")
    }.observeOn(AndroidSchedulers.mainThread())
    //회원가입
    fun users(usersRequest: UsersRequest) = apiTest.users(usersRequest).doOnNext {
        //header에 token을 jwt로 변경
        repositoryCached.setValue(LocalKey.TOKEN, it.jwt)
    }
}