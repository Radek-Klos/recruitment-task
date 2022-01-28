package com.peye.characters.domain.entity

data class Character(
    val name: String,
    val status: String,
    val currentLocationName: String,
    val originLocationName: String,
    val portraitImageUrl: String?
)
