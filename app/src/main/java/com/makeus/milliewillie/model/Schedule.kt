package com.makeus.milliewillie.model

data class Schedule(
    val result: Result
): BaseResponse() {
    data class Result(
        val scheduleId: Long,
        val color: String,
        val distinction: String,
        val title: String,
        val startDate: String,
        val endDate: String,
        val repetition: String,
        val push: String,
        val ordinaryLeave: ArrayList<OrdinaryLeaveRes>,
        val toDoList: ArrayList<WorkRes>
    )
}

data class OrdinaryLeaveRes(val startDate: String,
                            val endDate: String)

data class WorkRes(
    val content: String,
    val processingStatus: String
)