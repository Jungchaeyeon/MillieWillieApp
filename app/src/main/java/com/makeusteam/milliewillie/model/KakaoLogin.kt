package com.makeusteam.milliewillie.model

data class KakaoLogin(
    val result : Result
) : BaseResponse() {
    data class Result(
        val isMember : Boolean=false,
        val jwt : String
    )
}

