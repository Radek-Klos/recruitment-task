package com.peye.characters.data.db

import android.content.Context
import androidx.room.Room
import com.peye.characters.data.db.dao.CharacterDao
import org.koin.core.module.Module

private const val CHARACTER_DB_FILENAME = "character.db"

fun Module.databaseModule() {
    single { provideCharacterDatabase(get()) }
    single { provideCharacterDao(get()) }
}

private fun provideCharacterDatabase(context: Context): CharacterDatabase =
    Room.databaseBuilder(
        context,
        CharacterDatabase::class.java,
        CHARACTER_DB_FILENAME
    ).build()

fun provideCharacterDao(characterDatabase: CharacterDatabase): CharacterDao =
    characterDatabase.characterDao()
