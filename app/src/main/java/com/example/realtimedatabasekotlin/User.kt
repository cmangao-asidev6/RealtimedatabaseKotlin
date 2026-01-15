package com.example.realtimedatabasekotlin

data class User(
    val firstName : String? = null,
    val lastName : String? = null,
    val age : String? = null,
    val userName : String? = null,
    val password : String? = null,
    val latitude : Double? = null,
    val longitude : Double? = null
)