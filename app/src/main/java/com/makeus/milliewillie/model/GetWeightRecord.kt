package com.makeus.milliewillie.model

data class GetWeightRecord(
    val result: GetExerciseWeightRecordRes
): BaseResponse() {
    data class GetExerciseWeightRecordRes(
        val goalWeight: Double,
        val monthWeight: ArrayList<Double>,
        val monthWeightMonth: ArrayList<String>,
        val dayWeightDay: ArrayList<String>,
        val dayWeight: ArrayList<String>,
        val dayDif: ArrayList<Double>
    )
}