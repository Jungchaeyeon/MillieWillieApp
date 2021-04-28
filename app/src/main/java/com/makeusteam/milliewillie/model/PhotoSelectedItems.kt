package com.makeusteam.milliewillie.model

data class PhotoSelectedItems(
    val uri: String,
    var isThisItem: Int = 0,
    var isCheck: Boolean = false
)