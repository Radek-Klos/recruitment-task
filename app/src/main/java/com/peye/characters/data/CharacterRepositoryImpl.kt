package com.peye.characters.data

import com.peye.characters.data.api.CharacterApiService
import com.peye.characters.data.api.CharacterApiService.Companion.STARTING_PAGE_INDEX
import com.peye.characters.domain.CharacterRepository
import com.peye.characters.domain.entity.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.atomic.AtomicBoolean

// todo refactor?
class CharacterRepositoryImpl(
    private val remoteApiService: CharacterApiService
) : CharacterRepository {

    private val characters = MutableSharedFlow<List<Character>>(replay = 1)

    private val inMemoryCache = mutableListOf<Character>()

    private var lastRequestedPage = 0

    private var isLoading = AtomicBoolean(false)

    override val isRequestInProgress = MutableSharedFlow<Boolean>(replay = 1)

    override suspend fun getCharactersStream(): Flow<List<Character>> {
        lastRequestedPage = STARTING_PAGE_INDEX
        inMemoryCache.clear()

        isRequestInProgress.emit(isLoading.get())
        try {
            fetchCharactersSinglePage(STARTING_PAGE_INDEX)
        } finally {
            isRequestInProgress.emit(false.also(isLoading::set))
        }
        return characters
    }

    private suspend fun fetchCharactersSinglePage(pageNumber: Int) {
        val page = remoteApiService.getCharacters(pageNumber).results.map {
            Character(it.name)
        }
        inMemoryCache.addAll(page)
        characters.emit(inMemoryCache)
    }

    override suspend fun seekMoreCharactersIfAvailable() {
        if (isLoading.getAndSet(true)) return
        isRequestInProgress.emit(isLoading.get())
        try { fetchCharactersSinglePage(++lastRequestedPage) }
        finally { isRequestInProgress.emit(false.also(isLoading::set)) }
    }
}
