package com.makeus.milliewillie.repository

import com.makeus.milliewillie.model.*
import com.makeus.milliewillie.network.api.Api
import com.makeus.milliewillie.repository.local.LocalKey
import com.makeus.milliewillie.repository.local.RepositoryCached

class ApiRepository(
    private val repositoryCached: RepositoryCached,
    private val apiTest: Api
) {
    // observeOn() , subscribeOn() 차이
   // fun gallerySearchList(keyword : String) = apiTest.gallerySearchList(keyword = keyword).observeOn(AndroidSchedulers.mainThread())


    fun kakaoLogin() = apiTest.kakaoLogin().doOnNext {
        //header에 token을 jwt로 변경
        repositoryCached.setValue(LocalKey.TOKEN , it.result.jwt)
    }.switchMap {
        //jwt API 호출
        apiTest.jwt()
    }

    //fun users(name : String) = apiTest.users(name)

    fun users(usersRequest: UsersRequest) = apiTest.users(usersRequest)

    fun schedule(body: ScheduleRequest) = apiTest.schedule(body)

    fun getDailyWeight(path: Long) = apiTest.getDailyWeight(path)

    fun postFirstWeight(body: FirstWeightRequest) = apiTest.postFirstWeight(body)
    fun postDailyWeight(body: PostDailyWeightRequest, path: Long) = apiTest.postDailyWeight(body, path)

    fun patchGoalWeight(body: PatchGoalWeightRequest, path: Long) = apiTest.patchGoalWeight(body = body, exerciseId = path)
}