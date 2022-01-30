package com.peye.characters.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.peye.characters.data.db.entity.CharacterEntity

@Dao
interface CharacterDao {

    @Query("SELECT * FROM CharacterEntity")
    suspend fun getAllCharacters(): List<CharacterEntity>

    @Insert
    suspend fun saveCharacter(character: CharacterEntity)
}
