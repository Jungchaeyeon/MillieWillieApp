package com.makeus.milliewillie.model

data class ErrorResponse(
    val code: Int,
    val name: String,
    val message: String,
)