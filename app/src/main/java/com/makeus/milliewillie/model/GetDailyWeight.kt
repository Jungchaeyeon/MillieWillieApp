package com.makeus.milliewillie.model

class GetDailyWeight(
    val result: Result
): BaseResponse() {
    data class Result(
        val goalWeight: String,
        val dailyWeightList: ArrayList<DailyWeight>,
        val weightDayList: ArrayList<WeightDay>
    )
}

data class DailyWeight(
    val dailyWeight: String
)

data class WeightDay(
    val weightDay: String
)