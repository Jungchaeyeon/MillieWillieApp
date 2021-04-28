package com.makeusteam.milliewillie.model

data class Plan(
    val types : Types,
    val todos : Todos
){
    data class Types(
        val planType : String=""
    )
    data class Todos(
        var todo : String=""
    )
}
