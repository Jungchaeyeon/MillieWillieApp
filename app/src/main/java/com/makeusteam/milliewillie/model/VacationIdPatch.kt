package com.makeusteam.milliewillie.model

import java.io.Serializable

data class VacationIdPatch(
    var useDays: Int?=null,
    var totalDays:Int?=null
) : Serializable