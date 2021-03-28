package com.makeus.milliewillie.model

data class PlansGet(
    val result: Result
) : BaseResponse() {
    data class Result(
        val planId: Long=0L,
        val startDate: String="",
        val endDate: String="",
        val dateInfo: String="",
        val planType: String="",
        val work: ArrayList<Work> =  ArrayList<Work>(),
        val diary: ArrayList<Diary> =  ArrayList<Diary>(),
    ){
        data class Work(
         val workId: Long=0L,
         val content: String="",
         val processingStatus: String=""
        )
        data class Diary(
            val diaryId: Long=0L,
            var date: String="",
            var title : String="",
            var content: String =""
        )
    }
}
