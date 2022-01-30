package com.peye.characters.domain

import com.peye.characters.domain.entity.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    val isRequestInProgress: Flow<Boolean>

    suspend fun getRemoteCharactersStream(): Flow<List<Character>>

    suspend fun seekMoreCharactersIfAvailable()

    suspend fun getLocallyStoredCharacters(): List<Character>

    suspend fun saveCharacterLocally(character: Character)
}
