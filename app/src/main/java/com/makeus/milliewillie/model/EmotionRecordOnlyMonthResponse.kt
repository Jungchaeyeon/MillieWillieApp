package com.makeus.milliewillie.model



data class EmotionRecordOnlyMonthResponse(
   var result: List<Result> = arrayListOf()
):BaseResponse(){
       data class Result(
          var date : String ="",
          var emotion : Int =0
       )

}

