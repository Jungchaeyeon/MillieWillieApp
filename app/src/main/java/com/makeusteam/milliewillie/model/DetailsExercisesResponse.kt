package com.makeusteam.milliewillie.model

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class DetailsExercisesResponse(
    val result: JsonObject
): BaseResponse() {
    data class GetExerciseRoutineRes(
        val routineName: String,
        val bodyPart: String,
        val repeatDay: JsonArray,
        val detailResList: JsonArray
    )
}

data class ExerciseDetailRes(
    val exerciseName: String,
    val exerciseType: Int,
    val setCount: Int,
    val isSetSame: Boolean,
    val setDetailList: ArrayList<ExerciseDetailSetRes>
)

data class ExerciseDetailSetRes(
    val setStr: String,
    val weight: Double,
    val count: Int,
    val time: Int
)