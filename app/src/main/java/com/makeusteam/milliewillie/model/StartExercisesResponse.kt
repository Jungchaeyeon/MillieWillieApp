package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName

data class StartExercisesResponse(
    @SerializedName("result") val result: Result
): BaseResponse() {
    data class Result(
        val routineName: String,
        val repeatDay: String,
        val exerciseList: ArrayList<DetailInfo>
    )
}

data class DetailInfo(
    val exerciseName: String,
    val setInfoList: ArrayList<DetailSetInfo>
)

data class DetailSetInfo(
    val setCount: Int,
    val weight: String,
    val count: String,
    val time: String
)