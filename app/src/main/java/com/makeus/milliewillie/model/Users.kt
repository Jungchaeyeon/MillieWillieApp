package com.makeus.milliewillie.model

data class Users(
   val result: Result
) : BaseResponse(){
    data class Result(
        var userId : Long,
        var jwt : String
    )
}
