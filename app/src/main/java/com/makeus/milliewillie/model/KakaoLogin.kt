package com.makeus.milliewillie.model

data class KakaoLogin(
    val result : Result
) : BaseResponse() {
    data class Result(
        val isMember : Boolean,
        val jwt : String
    )
}

