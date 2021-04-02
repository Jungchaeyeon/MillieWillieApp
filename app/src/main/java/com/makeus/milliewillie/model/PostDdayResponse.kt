package com.makeus.milliewillie.model

import com.google.gson.annotations.SerializedName

data class PostDdayResponse(
    @SerializedName("result") val result: DdayRes
): BaseResponse() {
    data class DdayRes(
        val ddayId: Long,
        val ddayType: String,
        val title: String,
        val subtitle: String,
        val date: String,
        val link: String?,
        val choiceCalendar: String,
        val placeLat: String?,
        val placeLon: String?,
        val works: ArrayList<PostWorkRes>
    )
}

data class PostWorkRes(
    val workId: Long,
    val content: String,
    val processingStatus: String
)