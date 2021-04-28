package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostRoutineRequest(
    @SerializedName("routineName") var routineName: String,
    @SerializedName("bodyPart") var bodyPart: String,
    @SerializedName("repeatDay") var repeatDay: String,
    @SerializedName("detailName") var detailName: ArrayList<String>,
    @SerializedName("detailType") var detailType: ArrayList<Int>,
    @SerializedName("detailTypeContext") var detailTypeContext: ArrayList<String>,
    @SerializedName("detailSetEqual") var detailSetEqual: ArrayList<Boolean>,
    @SerializedName("detailSet") var detailSet: ArrayList<Int>
): Serializable