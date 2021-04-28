package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class FirstEntrances(
    @SerializedName("result") val result: Long
): BaseResponse()