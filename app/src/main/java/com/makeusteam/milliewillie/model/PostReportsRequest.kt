package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostReportsRequest(
    @SerializedName("routineId") var routineId: Long,
    @SerializedName("totalTime") var totalTime: String,
    @SerializedName("exerciseStatus") var exerciseStatus: String
): Serializable