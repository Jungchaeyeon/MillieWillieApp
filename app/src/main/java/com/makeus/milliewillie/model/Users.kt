package com.makeus.milliewillie.model

data class Users(
    var userId : Long,
    var jwt : String
) : BaseResponse()
