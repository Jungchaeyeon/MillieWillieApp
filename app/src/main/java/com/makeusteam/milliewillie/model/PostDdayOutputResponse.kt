package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class PostDdayOutputResponse(
    @SerializedName("result") val result: PostDiaryRes
): BaseResponse() {
    data class PostDiaryRes(
        val diaryId: Long,
        val date: String,
        val title: String,
        val content: String
    )
}