package com.makeus.milliewillie.model

import androidx.lifecycle.MutableLiveData

data class StartRecyclerItem(
    var exName: String,
    var exInfo: String,
    var circleList: ArrayList<StartRecyclerCircleItem>,
    var stack: Int = 0
)