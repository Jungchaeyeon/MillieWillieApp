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

    //감정일기기
   @POST("emotions-record")
    fun postEmotionsRecord(
        @Body body : EmotionsRecordRequest
    ): Observable<EmotionsRecordResponse>

    @PATCH("emotions-record/{emotionsRecordId}")
    fun patchEmotionsRecord(
        @Body body : EmotionsRecordRequest,
        @Path("emotionsRecordId") emotionsRecordId: Long
    ): Observable<EmotionsRecordResponse>
    @DELETE("emotions-record/{emotionsRecordId}")
    fun deleteEmotionsRecord(
        @Path("emotionsRecordId") emotionsRecordId: Long
    ): Observable<BaseResponse>

    @GET("emotions-record/day?date={date}")
    fun getEmotionsRecordDay(
        @Path("date") date: String
    ): Observable<EmotionsRecordDayResponse>

    @GET("emotions-record/month?month=")
    fun getEmotionsRecordMonth(
        @Path("month") month: String
    ): Observable<EmotionsRecordDayResponse>
    // Main
    @GET("main/users-calendars")
    fun getMain(): Observable<Main>

    //일정 API
    @POST("calendars/plans")
    fun plans(
        @Body body : PlansRequest
    ): Observable<Plans>

    @GET(" calendars/plans/{planId}")
    fun getPlans(@Path("planId") planId: Long): Observable<PlansGet>

    @DELETE("calendars/plans/{planId}")
    fun deletePlans(@Path("planId") planId: Long): Observable<BaseResponse>

    @PATCH("calendars/plans/plans-diary/{diaryId}")
    fun patchPlanDiary(
        @Body body: PlanDiaryRequest, @Path("diaryId") diaryId: Long): Observable<PlanDiary>

    @PATCH("calendars/plans/plans-work/{workId}")
    fun patchDiary(
        @Path("workId") workId: Long): Observable<PlansWork>


    // 휴가 API
    @GET("users/vacations")
    fun getVacation(): Observable<VacationIdResponse>

    @PATCH("users/vacations/{vacationId}")
    fun patchVacationId( @Body body : VacationIdPatch, @Path("vacationId") vacationId : Long): Observable<VacationPatchResponse>

    @GET("users")
    fun getUsers(): Observable<GetUsersResponse>
    //회원정보
    @GET("users")
    fun getUsersRes(): Observable<UsersResponse>

    @PATCH("users")
    fun patchUsers(
        @Body body : UsersPatch
    ) : Observable<UsersResponse>

    //로그인
    @POST("users/login-kakao")
    fun kakaoLogin(): Observable<KakaoLogin>
    @POST("users/login-google")
    fun googleLogin(): Observable<KakaoLogin>

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

    @POST("calendars/schedule")
    fun schedule(
        @Body body: ScheduleRequest
    ): Observable<Schedule>


    @POST("exercises/first-entrances")
    fun postFirstEntrances(): Observable<FirstEntrances>

    @POST("exercises/first-weights")
    fun postFirstWeight(@Body body: FirstWeightRequest): Observable<FirstWeight>

    @POST("exercises/{exerciseId}/weights")
    fun postDailyWeight(@Body body: PostDailyWeightRequest,
                        @Path("exerciseId") exerciseId: Long): Observable<PostDailyWeight>

    @POST("exercises/{exerciseId}/routines")
    fun postRoutine(@Body body: PostRoutineRequest,
                    @Path("exerciseId") exerciseId: Long): Observable<PostRoutine>

    @POST("exercises/{exerciseId}/routines/{routineId}/reports")
    fun postReports(@Path("exerciseId") exerciseId: Long,
                    @Path("routineId") routineId: Long,
                    @Body body: PostReportsRequest): Observable<PostReportsResponse>

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

    @GET("exercises/{exerciseId}/reports")
    fun getCalendarReports(@Path("exerciseId") exerciseId: Long,
                           @Query("viewYear") viewYear: Int,
                           @Query("viewMonth") viewMonth: Int):Observable<ReportsResponse>

    @GET("exercises/{exerciseId}/routines/{routineId}/detail-exercises")
    fun getDetailsExercises(@Path("exerciseId") exerciseId: Long,
                            @Path("routineId") routineId: Long): Observable<DetailsExercisesResponse>

    @GET("exercises/{exerciseId}/routines/{routineId}/start-exercises")
    fun getStartExercises(@Path("exerciseId") exerciseId: Long,
                          @Path("routineId") routineId: Long): Observable<StartExercisesResponse>
    @GET("exercises/{exerciseId}/routines/{routineId}/reports")
    fun getReports(@Path("exerciseId") exerciseId: Long,
                   @Path("routineId") routineId: Long,
                   @Query("reportDate") reportDate: String): Observable<GetReportsResponse>


    @PATCH("exercises/{exerciseId}/routines/{routineId}")
    fun patchRoutine(@Path("exerciseId") exerciseId: Long,
                     @Path("routineId") routineId: Long,
                     @Body body: PostRoutineRequest): Observable<PatchRoutine>

    @PATCH("exercises/{exerciseId}/goal-weights")
    fun patchGoalWeight(@Path("exerciseId") exerciseId: Long,
                        @Body body: PatchGoalWeightRequest): Observable<ResultResponse>

    @PATCH("exercises/{exerciseId}/weights")
    fun patchTodayWeight(@Path("exerciseId") exerciseId: Long,
                        @Body body: PatchTodayWeightRequest): Observable<ResultResponse>

    @PATCH("exercises/{exerciseId}/routines/{routineId}/reports")
    fun patchReports(@Path("exerciseId") exerciseId: Long,
                     @Path("routineId") routineId: Long,
                     @Body body: PatchReportsRequest): Observable<PatchRoutine>

    @PATCH("users")
    fun patchUsers(@Body body: PatchUsersRequest): Observable<PatchUsersResponse>

    @DELETE("exercises/{exerciseId}/routines/{routineId}")
    fun deleteRoutine(@Path("exerciseId") exerciseId: Long,
                      @Path("routineId") routineId: Long): Observable<ResultResponse>

    @DELETE("users")
    fun deleteUsers(): Observable<BaseResponse>

    @DELETE("exercises/{exerciseId}/routines/{routineId}/reports")
    fun deleteReports(@Path("exerciseId") exerciseId: Long,
                      @Path("routineId") routineId: Long,
                      @Query("reportDate") reportDate: String): Observable<PatchRoutine>

}