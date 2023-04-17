package com.example.rickyandmorty.domain.model.characters

import com.example.rickyandmorty.domain.model.locations.Locations

data class  Characters(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Locations,
    val name: String,
    val origin: Locations,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)