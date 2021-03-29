package com.makeus.milliewillie.model

import com.google.gson.JsonArray

data class ReportsResponse(
    val result: JsonArray
): BaseResponse()