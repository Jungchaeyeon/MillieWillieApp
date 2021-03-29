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


    fun getDailyWeight(path: Long) = apiTest.getDailyWeight(path)
    fun getWeightRecord(path: Long, viewMonth:Int, viewYear: Int) = apiTest.getWeightRecord(exerciseId =  path, viewMonth = viewMonth, viewYear = viewYear)
    fun getAllRoutines(path: Long) = apiTest.getAllRoutines(exerciseId = path)
    fun getRoutines(path: Long, targetDate: String) = apiTest.getRoutines(exerciseId = path, targetDate = targetDate)
    fun getCalendarReports(path: Long, viewYear: Int, viewMonth: Int) = apiTest.getCalendarReports(exerciseId = path, viewYear = viewYear, viewMonth = viewMonth)
    fun getDetailsExercises(exerciseId: Long, routineId: Long) = apiTest.getDetailsExercises(exerciseId = exerciseId, routineId = routineId)
    fun getStartExercises(exerciseId: Long, routineId: Long) = apiTest.getStartExercises(exerciseId = exerciseId, routineId = routineId)
    fun getReports(exerciseId: Long, routineId: Long, reportDate: String) = apiTest.getReports(exerciseId = exerciseId, routineId = routineId, reportDate = reportDate)
    fun getUsers() = apiTest.getUsers()

    fun postReports(exerciseId: Long, routineId: Long, body: PostReportsRequest) = apiTest.postReports(exerciseId = exerciseId, routineId = routineId, body = body)
    fun postFirstWeight(body: FirstWeightRequest) = apiTest.postFirstWeight(body)
    fun postDailyWeight(body: PostDailyWeightRequest, path: Long) = apiTest.postDailyWeight(body, path)
    fun postRoutine(body: PostRoutineRequest, path: Long) = apiTest.postRoutine(body = body, exerciseId = path)

    fun patchRoutine(exerciseId: Long, routineId: Long, body: PostRoutineRequest) = apiTest.patchRoutine(exerciseId = exerciseId, routineId = routineId, body = body)
    fun patchGoalWeight(body: PatchGoalWeightRequest, path: Long) = apiTest.patchGoalWeight(body = body, exerciseId = path)
    fun patchTodayWeight(path: Long, body: PatchTodayWeightRequest) = apiTest.patchTodayWeight(exerciseId = path, body = body)
    fun patchReports(exerciseId: Long, routineId: Long, body: PatchReportsRequest) = apiTest.patchReports(exerciseId = exerciseId, routineId = routineId, body = body)
    fun patchUsers(body: PatchUsersRequest) = apiTest.patchUsers(body = body)

    fun deleteRoutine(exerciseId: Long, routineId: Long) = apiTest.deleteRoutine(exerciseId = exerciseId, routineId = routineId)
    fun deleteUsers() = apiTest.deleteUsers()
    fun deleteReports(exerciseId: Long, routineId: Long, reportDate: String) = apiTest.deleteReports(exerciseId = exerciseId, routineId = routineId, reportDate = reportDate)

    fun plans(plansRequest: PlansRequest) = apiTest.plans(plansRequest).doOnNext {
        Log.e(it.result.color.toString(),"왜 안돼?")
    }
    //회원가입
    fun users(usersRequest: UsersRequest) = apiTest.users(usersRequest).doOnNext {
        //header에 token을 jwt로 변경
        repositoryCached.setValue(LocalKey.TOKEN, it.jwt)
    }
}