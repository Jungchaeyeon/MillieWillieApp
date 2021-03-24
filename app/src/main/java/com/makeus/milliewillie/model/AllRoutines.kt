package com.makeus.milliewillie.model

import com.google.gson.JsonArray

data class AllRoutines(
    val result: JsonArray
): BaseResponse()

data class MyRoutineInfo(
    val routineName: String,
    val routineRepeatDay: String,
    val routineId: Long,
    val isDoneRoutine: String = "false"
)