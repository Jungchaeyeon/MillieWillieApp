package com.makeus.milliewillie.model

data class FirstWeight(
    val result: Result
): BaseResponse() {
    data class Result(
        val exerciseId: Long
    )
}