package com.makeus.milliewillie.model

data class ReportRecyclerItem(
    val exerciseName: String,
    val exerciseOption: String,
    val exerciseStatus: String,
    val doneList: ArrayList<ReportInnerRecyclerItem>,
    val isDone: Boolean,
    val doneSet: Int
)