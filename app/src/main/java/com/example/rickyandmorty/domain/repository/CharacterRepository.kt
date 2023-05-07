package com.example.rickyandmorty.domain.repository

import com.example.rickyandmorty.domain.models.character.CharacterModel
import com.example.rickyandmorty.domain.models.character.CharacterResult

interface CharacterRepository {

    suspend fun getCharacter(page: Int, name: String, status: String, gender: String, species: String): CharacterModel

    suspend fun insertCharacter(list: List<CharacterResult>)

    suspend fun getListCharacters(): List<CharacterResult>

}