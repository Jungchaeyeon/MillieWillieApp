package com.makeus.milliewillie.model

data class WeightPerDay(
    var dayOfMonth: String,
    var currentWeight: String = "정보없음",
    var PMAmount: String = "·"
)