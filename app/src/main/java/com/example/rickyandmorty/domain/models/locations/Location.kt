package com.example.rickyandmorty.domain.models.locations

data class Location(
    val id: Int,
    val name: String,
    val created: String,
    val dimension: String,
    val residents: List<String>,
    val type: String,
    val url: String
)