package com.makeus.milliewillie.model

import java.io.Serializable

data class EmotionsRecordRequest(
   var content : String = "",
   var emotion : Int = 0
):Serializable
