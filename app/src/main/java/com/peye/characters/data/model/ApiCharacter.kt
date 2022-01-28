package com.peye.characters.data.model

data class ApiCharacter(
    val name: String,
    val status: String,
    val origin: ApiLocation,
    val location: ApiLocation,
    val image: String
)
