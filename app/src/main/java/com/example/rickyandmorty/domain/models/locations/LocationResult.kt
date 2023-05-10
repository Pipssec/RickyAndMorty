package com.example.rickyandmorty.domain.models.locations

data class LocationResult(
    val created: String,
    val dimension: String,
    val id: Int,
    val name: String,
    val residents: List<String>,
    val type: String,
    val url: String
)
