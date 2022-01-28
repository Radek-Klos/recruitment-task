package com.peye.characters.domain

import com.peye.characters.domain.entity.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    val isRequestInProgress: Flow<Boolean>

    suspend fun getCharactersStream(): Flow<List<Character>>

    suspend fun seekMoreCharactersIfAvailable()
}
