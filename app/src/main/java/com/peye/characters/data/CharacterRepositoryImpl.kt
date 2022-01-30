package com.peye.characters.data

import com.peye.characters.data.api.CharacterApiService
import com.peye.characters.data.api.CharacterApiService.Companion.STARTING_PAGE_INDEX
import com.peye.characters.data.db.dao.CharacterDao
import com.peye.characters.data.db.entity.CharacterEntity
import com.peye.characters.domain.CharacterRepository
import com.peye.characters.domain.entity.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.concurrent.atomic.AtomicBoolean

// todo refactor?
class CharacterRepositoryImpl(
    private val remoteApiService: CharacterApiService,
    private val characterDao: CharacterDao
) : CharacterRepository {

    private val characters = MutableSharedFlow<List<Character>>(replay = 1)

    private val inMemoryCache = mutableListOf<Character>()

    private var lastRequestedPage = 0

    private var isLoading = AtomicBoolean(false)

    override val isRequestInProgress = MutableSharedFlow<Boolean>(replay = 1)

    override suspend fun getRemoteCharactersStream(): Flow<List<Character>> {
        if (isLoading.getAndSet(true)) return characters

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
            Character(it.name, it.status, it.location.name, it.origin.name, it.image)
        }
        inMemoryCache.addAll(page)
        characters.emit(inMemoryCache)
    }

    override suspend fun seekMoreCharactersIfAvailable() {
        if (isLoading.getAndSet(true)) return

        isRequestInProgress.emit(isLoading.get())
        try {
            fetchCharactersSinglePage(++lastRequestedPage)
        } finally {
            isRequestInProgress.emit(false.also(isLoading::set))
        }
    }

    override suspend fun getLocallyStoredCharacters(): List<Character> {
        val characters = characterDao.getAllCharacters().map {
            Character(it.name, it.status, it.location, it.origin)
        }
        return characters
    }

    override suspend fun saveCharacterLocally(character: Character) = with(character) {
        val characterEntity = CharacterEntity(name, status, currentLocationName, originLocationName)
        characterDao.saveCharacter(characterEntity)
    }
}
