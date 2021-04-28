package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class PostReportsResponse(
    @SerializedName("result") val result: String
): BaseResponse()