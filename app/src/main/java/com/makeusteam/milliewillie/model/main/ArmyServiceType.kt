package com.makeusteam.milliewillie.model.main

import com.google.gson.annotations.SerializedName

enum class ArmyServiceType(val label : String) {
    @SerializedName("1")
    SOLDIER("일반병사"),

    @SerializedName("2")
    NCO("부사관"),

    @SerializedName("3")
    WO("준사관"),

    @SerializedName("4")
    CAPTAIN("장교")


}

