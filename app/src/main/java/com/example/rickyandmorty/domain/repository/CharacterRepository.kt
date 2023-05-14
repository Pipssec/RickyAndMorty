package com.example.rickyandmorty.domain.repository

import com.example.rickyandmorty.domain.models.character.CharacterModel
import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import io.reactivex.Observable

interface CharacterRepository {

    suspend fun getCharacter(
        page: Int,
        name: String,
        status: String,
        gender: String,
        species: String
    ): CharacterModel

    suspend fun insertCharacter(list: List<CharacterResult>)

    suspend fun getListCharacters(
        offset: Int,
        limit: Int,
        name: String,
        gender: String,
        status: String,
        species: String
    ): List<CharacterResult>

    fun getDetailEpisode(id: String): Observable<List<EpisodeResult>>

}