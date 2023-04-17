package com.example.rickyandmorty.data.response.characters

import com.example.rickyandmorty.domain.model.characters.Characters

data class CharactersResponse(
    val info: Info,
    val results: List<Characters>
)