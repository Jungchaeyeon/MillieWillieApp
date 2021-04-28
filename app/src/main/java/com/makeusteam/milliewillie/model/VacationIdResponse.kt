package com.makeusteam.milliewillie.model

data class VacationIdResponse(
    val result: List<Result>
) : BaseResponse(){
    data class Result(
     val vacationId : Long,
     val title : String,
     val useDays : Int,
     val totalDays : Int
    )
}

