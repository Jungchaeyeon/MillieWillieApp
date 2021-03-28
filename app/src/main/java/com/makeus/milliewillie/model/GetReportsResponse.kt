package com.makeus.milliewillie.model

import com.google.gson.annotations.SerializedName

data class GetReportsResponse(
    @SerializedName("result") val result: Result
): BaseResponse() {
    data class Result(
        val totalTime: String,
        val reportDate: String,
        val reportText: String,
        val exerciseList: ArrayList<RoutineExercise>
    )
}

data class RoutineExercise(
    val exerciseName: String,
    val exerciseStatus: String,
    val doneSet: Int,
    val isDone: Boolean,
    val detailList: ArrayList<ExerciseDetail>
)

data class ExerciseDetail(
    val setCount: Int,
    val weight: String,
    val time: String
)