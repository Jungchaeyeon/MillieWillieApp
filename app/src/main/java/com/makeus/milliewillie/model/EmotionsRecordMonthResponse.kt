package com.makeus.milliewillie.model



data class EmotionsRecordMonthResponse(
   val result: Result = Result()
):BaseResponse(){
       data class Result(
           var month : ArrayList<Month>? = null,
           var today : Today?= null
       ){
           data class Month(
               val date : String="",
               val emotion : Int=0
           )
           data class Today(
               val emotionRecordId : Long=0L,
               val date : String="",
               val content : String="",
               val emotion : Int=0,
               val emotionText : String=""
           )

       }

}

