package com.makeus.milliewillie.network.api

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


interface Api {

//    @GET("openapi/service/rest/PhotoGalleryService/gallerySearchList")
//    fun gallerySearchList(
//        @Query("numOfRows") numOfRows : Int = 20,
//        @Query("pageNo") pageNo : Int = 1,
//        @Query("MobileOS") MobileOS : String = "AND",
//        @Query("MobileApp") MobileApp : String = "AppTest",
//        @Query("ServiceKey" , encoded = true) ServiceKey : String = "LfK5Z3og8LzQNxddM%2FbBwctUVcpLk1qTBvzPsJK2SNuqPrPxz%2Fy9lrdhnsCO4t2KRpYX1RZQ1ZthK9NEPYOQGg%3D%3D",
//        @Query("arrange") arrange : String = "D",
//        @Query("keyword" , encoded = true) keyword : String,
//        @Query("_type") _type : String = "json"
//    ) : Observable<GallerySearch>

//    @POST("user/login")
//    fun login(
//        @Body userData: UserInfo.Data = UserInfo.Data()
//    ): Single<UserInfo>
//
//    @GET("v1/nid/me")
//    fun naverLogin(
//        @Header("Authorization") token: String
//    ): Observable<NaverUserInfo>
//
//    @Multipart
//    @POST("upload/thumbnail")
//    fun uploadThumbnail(
//        @Part file: MultipartBody.Part
//    ): Observable<Thumbnail>
}