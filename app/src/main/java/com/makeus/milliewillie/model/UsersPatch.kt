package com.makeus.milliewillie.model

import java.io.Serializable

data class UsersPatch(
    var serveType: String = "",
    var startDate: String = "",
    var endDate: String = "",
    var strPrivate: String = "",
    var strCorporal: String = "",
    var strSergeant: String = "",
    var proDate: String? = ""
) : Serializable
