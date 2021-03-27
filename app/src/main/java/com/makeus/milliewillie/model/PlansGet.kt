package com.makeus.milliewillie.model

data class PlansGet(
    val result: Result
) : BaseResponse() {
    data class Result(
        val planId: Long,
        val startDate: String,
        val endDate: String,
        val dateInfo: String,
        val planType: String,
        val work: List<Work>,
        val diary: List<Diary>,
    ){
        data class Work(
         val workId: Long,
         val content: String,
         val processingStatus: String
        )
        data class Diary(
            val diaryId: Long,
            val date: String,
            val title : String,
            val content: String
        )
    }
}
