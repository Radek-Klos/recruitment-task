package com.peye.characters.ui.chardetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peye.characters.domain.entity.Character

class CharacterDetailsViewModel(character: Character) : ViewModel() {

    val characterName = MutableLiveData(character.name)

    val portraitImageUrlOrNull = MutableLiveData(character.portraitImageUrl)

}
