package com.peye.characters.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Character(
    val name: String,
    val status: String,
    val currentLocationName: String,
    val originLocationName: String,
    val portraitImageUrl: String?
) : Parcelable
