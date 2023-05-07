package com.example.rickyandmorty.domain.models.character

data class CharacterModel (
    var info: CharacterInfo,
    var result: List<CharacterResult>
    )