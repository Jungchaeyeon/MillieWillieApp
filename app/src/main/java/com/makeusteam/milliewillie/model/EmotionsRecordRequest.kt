package com.makeusteam.milliewillie.model

import java.io.Serializable

data class EmotionsRecordRequest(
   var date : String ="",
   var content : String = "",
   var emotion : Int = 0
):Serializable
