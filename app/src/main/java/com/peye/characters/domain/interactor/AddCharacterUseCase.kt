package com.peye.characters.domain.interactor

import com.peye.characters.domain.CharacterRepository
import com.peye.characters.domain.entity.Character

class AddCharacterUseCase(
    private val repository: CharacterRepository
) {

    suspend fun addNewCharacter(itsName: String, itsStatus: String, itsLocation: String, itsOrigin: String) {
        val character = Character(itsName, itsStatus, itsLocation, itsOrigin)
        repository.saveCharacterLocally(character)
    }

    fun isValidInput(inputContent: String?) =
        inputContent?.isNotBlank() ?: false
}
