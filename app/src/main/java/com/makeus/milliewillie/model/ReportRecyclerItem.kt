package com.makeus.milliewillie.model

data class ReportRecyclerItem(
    val exerciseName: String,
    val exerciseOption: String,
    val exerciseComplete: String,
    val doneList: ArrayList<ReportInnerRecyclerItem>
)