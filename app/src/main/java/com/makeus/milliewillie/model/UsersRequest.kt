package com.makeus.milliewillie.model

import java.io.Serializable

data class UsersRequest(
    var name : String="",
    var stateIdx : Int=1,
    var serveType : String="",
    var startDate : String="",
    var endDate : String="",
    var strPrivate : String?="",
    var strCorporal : String?="",
    var strSergeant : String?="",
    var proDate : String?="",
    var goal : String="",
    val socialType : String="",
) : Serializable
