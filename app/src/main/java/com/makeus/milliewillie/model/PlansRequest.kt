package com.makeus.milliewillie.model

import java.io.Serializable

data class PlansRequest(
    var color: String = "",
    var planType: String = "",
    var title: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var push: String? = null,
    var pushDeviceToken: String? = null,
    var planVacation: ArrayList<PlanVacation> = arrayListOf(),
    var work: List<Work>? = null
) : Serializable {
    data class PlanVacation(
        var vacationId: Long? = 0L,
        var count: Int? = 0
    )

    data class Work(
        var content: String = ""
    )
}
