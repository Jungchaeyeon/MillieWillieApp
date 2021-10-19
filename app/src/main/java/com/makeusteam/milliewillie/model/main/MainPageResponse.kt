package com.makeusteam.milliewillie.model.main

import com.google.gson.annotations.SerializedName
import com.makeusteam.milliewillie.model.BaseResponse

data class MainPageResponse(
    @SerializedName("result") val result: Result
): BaseResponse(){
    data class Result(
      val name : String,
      val profileImg : String?,
      val birthday : String?,
      val stateIdx : ArmyServiceType,
      val serveType : String,
      val startDate : String,
      val endDate : String,
      val strPrivate : String,
      val strCorporal : String,
      val strSergeant : String,
      val hobong : Int,
      val normalPromotionStateIdx : PromotionState,
      val proDate : String? ,
      val goal : String?,
      val vacationTotalDays : Int,
      val vacationUseDays : Int,
      val dday : List<DdayModel>,
      val plan : List<PlanMain>
    ){

      fun toModel(allDday : String, hodongDday : String, promotionDday: String, nextClass : PromotionState, percent: Float)= MainPageModel(
          name = name,
          profileImg = profileImg,
          birthday = birthday,
          stateIdx = stateIdx,
          serveType = serveType,
          startDate = startDate,
          endDate = endDate,
          strPrivate = strPrivate,
          strCorporal = strCorporal,
          strSergeant = strSergeant,
          nextClass = nextClass,
          AllDday=allDday,
          hobongDday= hodongDday,
          promotionDday = promotionDday,
          serviceTimePercent = percent,
          hobong = hobong.toString(),
          normalPromotionStateIdx = normalPromotionStateIdx,
          proDate = proDate,
          goal = goal,
          vacationTotalDays = vacationTotalDays.toString(),
          vacationUseDays = vacationUseDays.toString(),
          dday = dday,
          plan = plan
        )

    }
}