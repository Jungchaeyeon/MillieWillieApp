package com.makeus.milliewillie.model

import com.google.gson.annotations.SerializedName

data class FirstWeight(
    @SerializedName("result") val result: Long
): BaseResponse()