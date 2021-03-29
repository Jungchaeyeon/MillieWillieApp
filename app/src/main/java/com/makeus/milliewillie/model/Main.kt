package com.makeus.milliewillie.model

data class Main(
    val result: Result
): BaseResponse(){
    data class Result(
      val name : String,
      val profileImg : String,
      val birthday : String,
      val stateIdx : Int,
      val serveType : String,
      val startDate : String,
      val endDate : String,
      val strPrivate : String,
      val strCorporal : String,
      val strSergeant : String,
      val hobong : Int,
      val normalPromotionStateIdx : Int,
      val proDate : String,
      val goal : String,
      val vacationTotalDays : Int,
      val vacationUseDays : Int,
      val dday : List<String>?
    )
}