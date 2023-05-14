package com.example.rickyandmorty.data.repository

import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.data.db.dao.CharacterDao
import com.example.rickyandmorty.data.mappers.CharacterMapper
import com.example.rickyandmorty.domain.models.character.CharacterModel
import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import com.example.rickyandmorty.domain.repository.CharacterRepository
import io.reactivex.Observable
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val apiService: NetworkApi,
    private val characterDao: CharacterDao,
    private val mapper: CharacterMapper
) : CharacterRepository {

    override suspend fun getCharacter(
        page: Int,
        name: String,
        status: String,
        gender: String,
        species: String
    ): CharacterModel {
        val characterApi = apiService.getAllCharacters(page, name, gender, status, species)
        val listCharacters = mapper.mapCharacterResponseForCharacter(characterApi)
        characterDao.insertCharacter(mapper.mapListResultResponseForListDb(listCharacters.result))
        return listCharacters
    }

    override suspend fun insertCharacter(list: List<CharacterResult>) {
        characterDao.insertCharacter(mapper.mapListResultResponseForListDb(list))
    }

    override suspend fun getListCharacters(
        offset: Int,
        limit: Int,
        name: String,
        gender: String,
        status: String,
        species: String
    ): List<CharacterResult> {
        return characterDao.getCharactersPage(offset, limit, name, gender, status, species)
            .map(mapper::mapCharacterResultDbForCharacterResult)
    }

    override fun getDetailEpisode(id: String): Observable<List<EpisodeResult>> {
        return apiService.getDetailEpisode(id)
    }


}