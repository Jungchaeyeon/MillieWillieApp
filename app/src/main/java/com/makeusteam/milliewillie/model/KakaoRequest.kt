package com.makeusteam.milliewillie.model

import java.io.Serializable

data class KakaoRequest(
   val name : String,
   val stateIdx : String,
   val serveType : String,
   val startDate : String,
   val endDate : String,
   val strPrivate : String,
   val strCorporal : String,
   val strSergeant : String,
   val proDate : String,
   val goal : String
):Serializable

