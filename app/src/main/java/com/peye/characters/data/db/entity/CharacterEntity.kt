package com.peye.characters.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharacterEntity(
    val name: String,
    val status: String,
    val location: String,
    val origin: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}
