package com.makeus.milliewillie.model

import java.io.Serializable

data class UsersRequest(
    val name : String,
    val serveType : String,
    val startDate : String,
    val endDate : String,
    val strPrivate : String,
    val strCorporal : String,
    val strSergeant : String,
    val proDate : String,
    val goal : String,
    val profileImg : String
) : Serializable
