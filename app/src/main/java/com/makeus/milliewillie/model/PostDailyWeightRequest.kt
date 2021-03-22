package com.makeus.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostDailyWeightRequest(
    @SerializedName("dayWeight") var dayWeight: Double
) : Serializable