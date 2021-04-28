package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class FirstWeight(
    @SerializedName("result") val result: String
): BaseResponse()