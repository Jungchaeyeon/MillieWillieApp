package com.makeus.milliewillie.model

import org.jsoup.Connection

data class EmotionsRecordDayResponse(
   val result: Result
):BaseResponse(){
   data class Result(
    val emotionRecordId : Long,
    val date : String ,
    val content : String ,
    val emotion : Int,
    val emotionText : String
   )
}
