package com.makeus.milliewillie.model

import com.google.gson.annotations.SerializedName

data class GetDdayResponse(
    @SerializedName("result") val result: GetDdayRes
): BaseResponse() {
    data class GetDdayRes(
        val ddayId: Long,
        val title: String,
        val date: String,
        val dateInfo: String,
        val ddayType: String,
        val choiceCalendarText: String?,
        val solarCalendarDate: String?,
        val work: ArrayList<GetWorkRes>,
        val diary: ArrayList<GetDiaryRes>
    )
}

data class GetWorkRes(
    val workId: Long,
    val content: String,
    val processingStatus: String
)

data class GetDiaryRes(
    val diaryId: Long,
    val date: String,
    val title: String,
    val content: String
)