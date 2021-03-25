package com.makeus.milliewillie.model

data class Plans(
    val result: Result
) : BaseResponse() {
    data class Result(
        val planId: Long,
        val color: String,
        val planType: String,
        val title: String,
        val startDate: String,
        val endDate: String,
        val push: String,
        val planVacation: List<PlanVacation>,
        val work: List<Work>,
    ){
        data class PlanVacation(
        val planVacationId : Long,
        val count : Int,
        val vacationId : Long
        )
        data class Work(
         val workId: Long,
         val content: String,
         val processingStatus: String
        )
    }
}
