package com.example.rickyandmorty.data.api.response.episode

import com.example.rickyandmorty.domain.models.episodes.Episode

data class EpisodeResponse(
    val info: Info,
    val results: List<Episode>
)
