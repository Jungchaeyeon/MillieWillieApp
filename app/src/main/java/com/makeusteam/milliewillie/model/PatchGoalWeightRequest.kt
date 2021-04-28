package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PatchGoalWeightRequest(
    @SerializedName("goalWeight") var goalWeight: Double
) : Serializable