package com.makeus.milliewillie.repository

import com.makeus.milliewillie.ActivityNavigator
import com.makeus.milliewillie.model.KakaoRequest
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

    //회원가입
    fun users(usersRequest: UsersRequest) = apiTest.users(usersRequest).doOnNext {
        //header에 token을 jwt로 변경
        Log.e(repositoryCached.getToken(),"현재 토큰 값아직 바뀌면 안됨")
        repositoryCached.setValue(LocalKey.TOKEN, it.jwt)
        Log.e(repositoryCached.getToken(),"여기서 jwt로 바껴야해")
    }
}