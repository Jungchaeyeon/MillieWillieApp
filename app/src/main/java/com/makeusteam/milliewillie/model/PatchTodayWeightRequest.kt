package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PatchTodayWeightRequest(
    @SerializedName("dayWeight") var dayWeight: Double,
    @SerializedName("dayDate") var dayDate: String
) : Serializable