package com.makeus.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FirstWeightRequest(
    @SerializedName("goalWeight") var goalWeight: Int,
    @SerializedName("firstWeight") var firstWeight: Int
) : Serializable