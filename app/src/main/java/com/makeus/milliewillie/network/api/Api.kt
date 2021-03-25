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
    fun postFirstWeight(@Body body: FirstWeightRequest): Observable<FirstWeight>

    @POST("exercises/{exerciseId}/weights")
    fun postDailyWeight(@Body body: PostDailyWeightRequest,
                        @Path("exerciseId") exerciseId: Long): Observable<PostDailyWeight>

    @POST("exercises/{exerciseId}/routines")
    fun postRoutine(@Body body: PostRoutineRequest,
                    @Path("exerciseId") exerciseId: Long): Observable<PostRoutine>

    @GET("exercises/{exerciseId}/daily-weights")
    fun getDailyWeight(@Path("exerciseId") exerciseId: Long): Observable<GetDailyWeight>

    @GET("exercises/{exerciseId}/weight-records")
    fun getWeightRecord(@Path("exerciseId") exerciseId: Long,
                        @Query("viewMonth") viewMonth: Int,
                        @Query("viewYear") viewYear: Int): Observable<GetWeightRecord>

    @GET("exercises/{exerciseId}/all-routines")
    fun getAllRoutines(@Path("exerciseId") exerciseId: Long): Observable<AllRoutines>

    @GET("exercises/{exerciseId}/routines")
    fun getRoutines(@Path("exerciseId") exerciseId: Long,
                    @Query("targetDate") targetDate: String): Observable<Routines>

    @PATCH("exercises/{exerciseId}/goal-weights")
    fun patchGoalWeight(@Path("exerciseId") exerciseId: Long,
                        @Body body: PatchGoalWeightRequest): Observable<ResultResponse>

    @PATCH("exercises/{exerciseId}/weights")
    fun patchTodayWeight(@Path("exerciseId") exerciseId: Long,
                        @Body body: PatchTodayWeightRequest): Observable<ResultResponse>

    @DELETE("exercises/{exerciseId}/routines/{routineId}")
    fun deleteRoutine(@Path("exerciseId") exerciseId: Long): Observable<ResultResponse>


    @POST("users/login-kakao")
    fun kakaoLogin(): Observable<KakaoLogin>

    @GET("users/jwt")
    fun jwt(): Observable<BaseResponse>

//    @POST("users")
//    fun users(
//        @Field("name") name : String
//    ): Observable<Users>

    @POST("calendars/plans")
    fun plans(
        @Body body : PlansRequest
    ): Observable<Plans>

    @POST("users")
    fun users(
        @Body body : UsersRequest
    ): Observable<Users>

}