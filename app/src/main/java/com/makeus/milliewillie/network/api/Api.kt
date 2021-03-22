package com.makeus.milliewillie.network.api

import com.makeus.milliewillie.model.*
import io.reactivex.Observable
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
//    ): Sin
//    gle<UserInfo>
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

    @POST("calendars/schedule")
    fun schedule(
        @Body body: ScheduleRequest
    ): Observable<Schedule>




    @POST("exercises/first-weights")
    fun postFirstWeight(@Body body: FirstWeightRequest
    ): Observable<FirstWeight>

    @POST("exercises/{exerciseId}/weights")
    fun postDailyWeight(@Body body: PostDailyWeightRequest,
                        @Path("exerciseId") exerciseId: Long
    ): Observable<PostDailyWeight>

    @GET("exercises/{exerciseId}/daily-weights")
    fun getDailyWeight(@Path("exerciseId") exerciseId: Long): Observable<GetDailyWeight>

    @PATCH("exercises/{exerciseId}/goal-weights")
    fun patchGoalWeight(@Path("exerciseId") exerciseId: Long,
                        @Body body: PatchGoalWeightRequest): Observable<PatchGoalWeight>

    @POST("users/login-kakao")
    fun kakaoLogin(): Observable<KakaoLogin>

    @GET("users/jwt")
    fun jwt(): Observable<BaseResponse>

//    @POST("users")
//    fun users(
//        @Field("name") name : String
//    ): Observable<Users>

    @POST("users")
    fun users(
        @Body body : UsersRequest
    ): Observable<Users>
}