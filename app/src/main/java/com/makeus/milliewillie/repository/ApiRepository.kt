package com.makeus.milliewillie.repository

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
        //header에 token을 jwt로 변경
        repositoryCached.setValue(LocalKey.TOKEN , it.result.jwt)
        if(!it.result.isMember){
            repositoryCached.setValue(LocalKey.ISMEMBER,false)
        }
        else{
            repositoryCached.setValue(LocalKey.ISMEMBER,true)
        }
    }.switchMap {
        //jwt API 호출
        apiTest.jwt()
    }
    fun jwt() = apiTest.jwt().doOnNext{}

    //fun users(name : String) = apiTest.users(name)

    //회원가입
    fun users(usersRequest: UsersRequest) = apiTest.users(usersRequest).doOnNext{
        //header에 token을 jwt로 변경
        repositoryCached.setValue(LocalKey.TOKEN , it.jwt)
    }.switchMap {
        //jwt API 호출
        apiTest.jwt()
    }
}