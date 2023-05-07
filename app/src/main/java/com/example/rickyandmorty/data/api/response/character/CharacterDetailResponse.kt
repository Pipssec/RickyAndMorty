package com.example.rickyandmorty.data.api.response.character

data class CharacterDetailResponse(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: CharacterLocationResponse,
    val name: String,
    val origin: CharacterOriginResponse,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)