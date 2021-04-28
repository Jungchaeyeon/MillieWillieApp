package com.makeusteam.milliewillie.model

data class CalendarDayResponse(
    val result:Result
): BaseResponse(){
    data class Result(
        val planCalendar : List<PlanCalendar>?,
        val ddayCalendar : List<String>?,
        val dday : List<Dday>?,
        val plan : List<Plan>?
    ){
        data class PlanCalendar(
         val color: String,
         val startDate: String,
         val endDate: String
        )
        data class Dday(
          val ddayId : Long?,
          val date : String?,
          val title : String?,
        )
        data class Plan(
           val planId:Long?,
           val title:String?,
           val color: String?
        )
    }
}
