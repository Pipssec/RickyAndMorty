package com.example.rickyandmorty.domain.model

data class Location(
    val created: String,
    val dimension: String,
    val id: Int,
    val name: String,
    val residents: List<Characters>,
    val type: String,
    val url: String
)