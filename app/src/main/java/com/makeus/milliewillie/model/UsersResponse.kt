package com.makeus.milliewillie.model

data class UsersResponse(
    val result: Result
) : BaseResponse() {
    data class Result(
        val userId: Long=0L,
        val name: String="",
        val profileImg: String="",
        val birthday: String="",
        val stateIdx: Int=0,
        val serveType: String="",
        var startDate: String="",
        val endDate: String="",
        val strPrivate: String="",
        val strCorporal: String="",
        val strSergeant: String="",
        var hobong: Int=0,
        val normalPromotionStateIdx: Int=0,
        val proDate: String="",
        val goal: String=""
    )
}
