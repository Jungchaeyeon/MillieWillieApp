package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FirstWeightRequest(
    @SerializedName("goalWeight") var goalWeight: Double = -1.0,
    @SerializedName("firstWeight") var firstWeight: Double = -1.0
) : Serializable