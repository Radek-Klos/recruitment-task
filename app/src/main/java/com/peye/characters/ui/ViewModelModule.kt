package com.peye.characters.ui

import com.peye.characters.domain.entity.Character
import com.peye.characters.ui.charcreation.CharacterCreationViewModel
import com.peye.characters.ui.chardetails.CharacterDetailsViewModel
import com.peye.characters.ui.charsdisplay.CharactersDisplayViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module

fun Module.viewModelModule() {
    viewModel { CharactersDisplayViewModel(Dispatchers.IO, get()) }
    viewModel { (character: Character) -> CharacterDetailsViewModel(character) }
    viewModel { CharacterCreationViewModel(Dispatchers.IO, get()) }
}
