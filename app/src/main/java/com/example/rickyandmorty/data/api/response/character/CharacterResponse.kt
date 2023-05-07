package com.example.rickyandmorty.data.api.response.character


data class CharacterResponse (
    val info: CharacterInfoResponse?,
    var results: List<CharacterResultResponse>
    )