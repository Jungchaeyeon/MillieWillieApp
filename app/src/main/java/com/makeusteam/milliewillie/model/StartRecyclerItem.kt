package com.makeusteam.milliewillie.model

data class StartRecyclerItem(
    var exName: String,
    var exInfo: String,
    var circleList: ArrayList<StartRecyclerCircleItem>,
    var stack: Int = 0
)