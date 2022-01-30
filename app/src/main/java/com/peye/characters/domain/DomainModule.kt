package com.peye.characters.domain

import com.peye.characters.data.CharacterRepositoryImpl
import com.peye.characters.domain.interactor.AddCharacterUseCase
import com.peye.characters.domain.interactor.GetCharactersUseCase
import org.koin.core.module.Module

fun Module.domainModule() {
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }

    single { GetCharactersUseCase(get()) }
    single { AddCharacterUseCase(get()) }
}
