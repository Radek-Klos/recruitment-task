package com.peye.characters.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peye.characters.data.db.dao.CharacterDao
import com.peye.characters.data.db.entity.CharacterEntity

@Database(
    entities = [
        CharacterEntity::class
    ],
    version = 1
)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
}
