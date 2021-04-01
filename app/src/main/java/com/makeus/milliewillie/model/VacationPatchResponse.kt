package com.makeus.milliewillie.model

import java.io.Serializable

data class VacationPatchResponse(
    val result: Result
) : BaseResponse(){
    data class Result(
     val vacationId : Long,
     val title : String,
     val useDays : Int,
     val totalDays : Int
    )
}

