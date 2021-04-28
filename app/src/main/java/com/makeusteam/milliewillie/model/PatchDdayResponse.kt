package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class PatchDdayResponse(
    @SerializedName("result") val result: PatchDdayRes
): BaseResponse() {
    data class PatchDdayRes(
        val ddayId: Long,
        val ddayType: String,
        val title: String,
        val subtitle: String,
        val date: String,
        val link: String,
        val choiceCalendar: String,
        val placeLat: String?,
        val placeLon: String?,
        val works: ArrayList<PatchWorkRes>
    )
}

data class PatchWorkRes(
    val workId: Long,
    val content: String,
    val processingStatus: String
)