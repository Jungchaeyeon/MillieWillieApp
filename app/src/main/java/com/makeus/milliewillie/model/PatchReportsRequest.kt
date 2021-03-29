package com.makeus.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PatchReportsRequest(
    @SerializedName("reportDate") var reportDate: String,
    @SerializedName("reportText") var reportText: String
): Serializable