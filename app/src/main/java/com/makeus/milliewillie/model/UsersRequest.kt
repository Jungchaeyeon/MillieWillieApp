package com.makeus.milliewillie.model

import java.io.Serializable

data class UsersRequest(
    val name : String,
    val stateIdx : Int,
    val serveType : String,
    val birthday : String,
    val startDate : String,
    val endDate : String,
    val strPrivate : String,
    val strCorporal : String,
    val strSergeant : String,
    val proDate : String,
    val goal : String,
    val socialType : String
) : Serializable
