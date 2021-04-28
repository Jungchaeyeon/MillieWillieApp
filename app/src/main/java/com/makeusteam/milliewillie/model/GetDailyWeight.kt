package com.makeusteam.milliewillie.model

import com.google.gson.JsonArray

class GetDailyWeight(
    val result: Result
): BaseResponse() {
    data class Result(
        val goalWeight: String,
        val dailyWeightList: JsonArray,
        val weightDayList: JsonArray
    )
}

data class DailyWeight(
    val dailyWeight: String
)

data class WeightDay(
    val weightDay: String
)