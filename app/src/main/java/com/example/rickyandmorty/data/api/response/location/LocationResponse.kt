package com.example.rickyandmorty.data.api.response.location

import com.example.rickyandmorty.domain.models.locations.Location

data class LocationResponse (
    val info: Info,
    val results: List<Location>
)