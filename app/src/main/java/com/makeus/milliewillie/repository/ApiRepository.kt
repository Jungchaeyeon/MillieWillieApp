package com.makeus.milliewillie.repository

import com.makeus.milliewillie.network.api.Api
import io.reactivex.android.schedulers.AndroidSchedulers

class ApiRepository(
    private val apiTest: Api
) {
    // observeOn() , subscribeOn() 차이
   // fun gallerySearchList(keyword : String) = apiTest.gallerySearchList(keyword = keyword).observeOn(AndroidSchedulers.mainThread())



}