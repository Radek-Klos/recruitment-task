package com.peye.characters.ui

import com.peye.characters.ui.charsdisplay.CharactersDisplayViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module

fun Module.viewModelModule() {
    viewModel { CharactersDisplayViewModel() }
}
