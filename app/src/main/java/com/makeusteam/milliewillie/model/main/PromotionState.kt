package com.makeusteam.milliewillie.model.main

import com.google.gson.annotations.SerializedName

enum class PromotionState(val label : String) {
    @SerializedName("1")
    PRIVATE("이병"),

    @SerializedName("2")
    FIRSTCLASS("일병"),

    @SerializedName("3")
    CORPORAL("상병"),

    @SerializedName("4")
    SERGENT("병장"),

    DISCHARGE("전역")
}
