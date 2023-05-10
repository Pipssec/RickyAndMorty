package com.example.rickyandmorty.domain.models.episodes

data class Episode(
    val info: EpisodeInfo,
    val results: List<EpisodeResult>
)