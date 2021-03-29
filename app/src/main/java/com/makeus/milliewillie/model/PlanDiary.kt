package com.makeus.milliewillie.model

data class PlanDiary(
   val result : Result,
):BaseResponse(){
    data class Result(
        val diaryId : Long,
        val date : String,
        val title : String,
        val content : String
    )
}
