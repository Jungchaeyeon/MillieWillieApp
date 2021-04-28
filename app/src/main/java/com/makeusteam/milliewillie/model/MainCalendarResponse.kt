package com.makeusteam.milliewillie.model

data class MainCalendarResponse(
    val result: Result
):BaseResponse(){
    data class Result(
        val planCalendar : List<PlanCalendar>,
        val ddayCalendar : List<String>
    ){
        data class PlanCalendar(
           val color : String,
           val startDate : String,
           val endDate : String
        )
    }
}
