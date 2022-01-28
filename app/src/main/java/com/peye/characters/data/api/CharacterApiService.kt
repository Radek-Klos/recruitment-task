package com.peye.characters.data.api

import com.peye.characters.data.model.ApiCharactersPageResult
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApiService {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int
    ): ApiCharactersPageResult

    companion object {
        const val STARTING_PAGE_INDEX = 1
    }
}
