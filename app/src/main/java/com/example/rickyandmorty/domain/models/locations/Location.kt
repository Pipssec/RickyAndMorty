package com.example.rickyandmorty.domain.models.locations

data class Location(
    val info: LocationInfo,
    val results: List<LocationResult>
)