package com.peye.characters.domain.interactor

import com.peye.characters.domain.CharacterRepository

class AddCharacterUseCase(
    private val repository: CharacterRepository
) {

    // TODO: add method for saving characters

    fun isValidInput(inputContent: String?) =
        inputContent?.isNotBlank() ?: false
}
