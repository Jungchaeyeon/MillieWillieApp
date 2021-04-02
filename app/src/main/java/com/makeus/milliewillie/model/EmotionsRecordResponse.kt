package com.makeus.milliewillie.model

import org.jsoup.Connection

data class EmotionsRecordResponse(
   val result: Result = Result()
):BaseResponse(){
   data class Result(
    val emotionRecordId : Long=0L,
    val date : String ="",
    val content : String ="",
    val emotion : Int=0,
    var emotionText : String=""
   )
}
