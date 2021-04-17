package com.makeus.milliewillie.model

import org.jsoup.Connection

data class EmotionsRecordResponse(
   val result: Result = Result()
):BaseResponse(){
   data class Result(
       var emotionRecordId : Long=0L,
       val date : String ="",
       val content : String ="",
       var emotion : Int=0,
       var emotionText : String=""
   )
}
