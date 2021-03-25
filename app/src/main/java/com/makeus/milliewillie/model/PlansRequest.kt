package com.makeus.milliewillie.model

import java.io.Serializable

data class PlansRequest(
    var color: String="",
    var planType: String="",
    var title: String="",
    var startDate: String="",
    var endDate: String="",
    var push: String?=null,
    var pushDeviceToken: String?=null,
    var planVacation: List<PlanVacation>?= null,
    var work: List<Work>?= null
) : Serializable {
    data class PlanVacation(
        var vacationId: Long,
        var count: Int
    )

    data class Work(
        var content: String
    )
}
