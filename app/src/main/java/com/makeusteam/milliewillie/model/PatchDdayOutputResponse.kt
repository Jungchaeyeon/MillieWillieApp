package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class PatchDdayOutputResponse(
    @SerializedName("result") val result: PatchDiaryRes
): BaseResponse() {
    data class PatchDiaryRes(
        val diaryId: Long,
        val date: String,
        val title: String,
        val content: String
    )
}