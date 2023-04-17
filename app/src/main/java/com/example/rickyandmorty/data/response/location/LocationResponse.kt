package com.example.rickyandmorty.data.response.location

import com.example.rickyandmorty.domain.model.locations.Locations

data class LocationResponse(
    val info: Info,
    val results: List<Locations>
)