package com.example.rickyandmorty.data.response.episodes

import com.example.rickyandmorty.domain.model.episodes.Episodes

data class EpisodesResponse(
    val info: Info,
    val results: List<Episodes>
)