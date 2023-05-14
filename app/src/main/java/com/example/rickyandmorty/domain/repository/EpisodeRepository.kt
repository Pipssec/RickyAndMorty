package com.example.rickyandmorty.domain.repository

import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.models.episodes.Episode
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import io.reactivex.Observable


interface EpisodeRepository {

    suspend fun getEpisode(page: Int, name: String, episode: String): Episode

    suspend fun insertEpisode(list: List<EpisodeResult>)

    suspend fun getListEpisodesDb(
        offset: Int,
        limit: Int,
        name: String,
        episode: String
    ): List<EpisodeResult>

    fun getDetailCharacter(id: String): Observable<List<CharacterResult>>
}