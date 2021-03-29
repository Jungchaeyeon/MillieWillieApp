package com.makeus.milliewillie.model

import java.io.Serializable

data class VacationIdPatch(
    var useDays: Int = 0,
    var totalDays:Int  = 0
) : Serializable