package com.example.rickyandmorty.data.api.response.character


data class CharacterResultResponse(
    var created: String?,
    var episode: List<String>?,
    var gender: String?,
    var id: Int?,
    var image: String?,
    var location: CharacterLocationResponse,
    var name: String?,
    var species: String?,
    var status: String?,
    var type: String?,
    var url: String?,
    var origin: CharacterOriginResponse
)
