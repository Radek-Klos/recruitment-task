package com.peye.characters.domain.interactor

import com.peye.characters.domain.CharacterRepository
import com.peye.characters.domain.entity.Character
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {

    val isLoading = repository.isRequestInProgress

    suspend fun startCharactersFetching(): Flow<List<Character>> {
        return repository.getRemoteCharactersStream()
    }

    suspend fun loadMoreCharactersIfPossible() {
        repository.seekMoreCharactersIfAvailable()
    }

    suspend fun getUserCreatedCharacters(): List<Character> {
        return repository.getLocallyStoredCharacters()
    }
}
