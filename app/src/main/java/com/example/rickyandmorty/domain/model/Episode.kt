package com.example.rickyandmorty.domain.model

data class Episode(
    val air_date: String,
    val characters: List<Characters>,
    val created: String,
    val episode: String,
    val id: Int,
    val name: String,
    val url: String
)