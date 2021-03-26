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
        var endDate: String="",
        var strPrivate: String="",
        var strCorporal: String="",
        var strSergeant: String="",
        var hobong: Int=0,
        val normalPromotionStateIdx: Int=0,
        val proDate: String="",
        val goal: String=""

    )
}
