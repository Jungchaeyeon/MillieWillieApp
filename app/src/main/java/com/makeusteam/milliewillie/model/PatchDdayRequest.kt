package com.makeusteam.milliewillie.model

import java.math.BigDecimal

data class PatchDdayRequest(
    val title: String,
    val subtitle: String,
    val date: String,
    val link: String,
    val choiceCalendar: String?,
    val placeLat: BigDecimal,
    val placeLon: BigDecimal,
    val work: ArrayList<String>
)