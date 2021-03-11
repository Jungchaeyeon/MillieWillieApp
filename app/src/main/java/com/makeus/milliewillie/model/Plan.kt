package com.makeus.milliewillie.model

import java.sql.Types

data class Plan(
    val types : Types,
    val todos : Todos
){
    data class Types(
        val planType : String=""
    )
    data class Todos(
        val todo : String=""
    )
}
