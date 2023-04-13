package com.example.rickyandmorty.data.response

import com.example.rickyandmorty.domain.model.Characters

data class CharactersResponse(
    val info: Info,
    val results: List<Characters>
)