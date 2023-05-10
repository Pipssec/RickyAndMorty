package com.example.rickyandmorty.domain.models.episodes

data class EpisodeInfo(
    val count: Int,
    val next: String?,
    val pages: Int,
    val prev: String
    )
