package com.makeus.milliewillie.model

data class PlansWork(
   val result : Result,
):BaseResponse(){
    data class Result(
    val workId : Long =0L,
    val content : String ="",
    val processingStatus : String =""
    )
}
