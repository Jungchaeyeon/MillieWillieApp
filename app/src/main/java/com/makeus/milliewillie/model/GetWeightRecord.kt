package com.makeus.milliewillie.model

import com.google.gson.JsonArray

data class GetWeightRecord(
    val result: GetExerciseWeightRecordRes
): BaseResponse() {
    data class GetExerciseWeightRecordRes(
        val goalWeight: Double,
        val monthWeight: JsonArray,
        val monthWeightMonth: JsonArray,
        val dayWeightDay: JsonArray,
        val dayWeight: JsonArray,
        val dayDif: JsonArray
    )
}