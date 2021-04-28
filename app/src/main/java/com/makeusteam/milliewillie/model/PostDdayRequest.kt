package com.makeusteam.milliewillie.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class PostDdayRequest(
    @SerializedName("ddayType") val ddayType: String,
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("date") val date: String,
    @SerializedName("link") val link: String?,
    @SerializedName("choiceCalendar") val choiceCalendar: String?,
    @SerializedName("placeLat") val placeLat: BigDecimal?,
    @SerializedName("placeLon") val placeLon: BigDecimal?,
    @SerializedName("work") val work: ArrayList<String?>
)