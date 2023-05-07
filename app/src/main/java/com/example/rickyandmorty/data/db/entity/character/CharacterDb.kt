package com.example.rickyandmorty.data.db.entity.character


data class CharacterDb(
    var info: CharacterInfoDb,
    var results: List<CharacterDbModel>
)
