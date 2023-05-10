package com.example.rickyandmorty.domain.repository

import com.example.rickyandmorty.domain.models.episodes.Episode
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult


interface EpisodeRepository {

    suspend fun getEpisode(page: Int, name: String, episode: String): Episode

    suspend fun insertEpisode(list: List<EpisodeResult>)

    fun getListEpisodesDb(): List<EpisodeResult>

}