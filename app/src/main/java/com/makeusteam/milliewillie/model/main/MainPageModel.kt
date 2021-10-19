package com.makeusteam.milliewillie.model.main

data class MainPageModel(

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
      val nextClass: PromotionState,
      val serviceTimePercent : Float,
      val AllDday : String,
      val hobongDday : String,
      val promotionDday : String,
      val hobong : String,
      val normalPromotionStateIdx : PromotionState,
      val proDate : String?,
      val goal : String?,
      val vacationTotalDays : String,
      val vacationUseDays : String,
      val dday : List<DdayModel>,
      val plan : List<PlanMain>
    )
