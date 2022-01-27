package com.peye.characters.app

import com.peye.characters.ui.viewModelModule
import org.koin.dsl.module

fun CharactersApplication.getAppModule() = appModule

private val appModule = module {
    viewModelModule()
}
