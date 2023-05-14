package com.example.rickyandmorty.data.mappers


import com.example.rickyandmorty.data.api.response.character.*
import com.example.rickyandmorty.data.db.entity.character.CharacterDbModel
import com.example.rickyandmorty.domain.models.character.*
import javax.inject.Inject

class CharacterMapper @Inject constructor() {
     private fun mapCharacterResponseForInfo(characterInfoResponse: CharacterInfoResponse? ) = CharacterInfo(
            pages = characterInfoResponse?.pages ?: ZERO_NUMBER,
            next = characterInfoResponse?.next ?: EMPTY_STRING,
            count = characterInfoResponse?.count ?: ZERO_NUMBER,
            prev = characterInfoResponse?.prev ?: EMPTY_STRING
    )

    private fun mapLocationResponseForLocation(characterLocationResponse: CharacterLocationResponse?) = CharacterLocation(
        name = characterLocationResponse?.name ?: EMPTY_STRING,
        url = characterLocationResponse?.url ?: EMPTY_STRING
    )

    fun mapResultResponseForResult(characterResultResponse: CharacterResultResponse?) = CharacterResult(
        id = characterResultResponse?.id ?: ZERO_NUMBER,
        created = characterResultResponse?.created ?: EMPTY_STRING,
        episode = characterResultResponse?.episode ?: emptyList(),
        gender = characterResultResponse?.gender ?: EMPTY_STRING,
        image = characterResultResponse?.image ?: EMPTY_STRING,
        name = characterResultResponse?.name ?: EMPTY_STRING,
        species = characterResultResponse?.species ?: EMPTY_STRING,
        status = characterResultResponse?.status ?: EMPTY_STRING,
        type = characterResultResponse?.type ?: EMPTY_STRING,
        url = characterResultResponse?.url ?: EMPTY_STRING,
        location = mapLocationResponseForLocation(characterResultResponse?.location),
        origin = mapOriginResponseForOrigin(characterResultResponse?.origin!!)
    )

    private fun mapListCharacterResponseForResult(list: List<CharacterResultResponse>) = list.map{
            mapResultResponseForResult(it)
    }

    fun mapCharacterResponseForCharacter(characterResponse: CharacterResponse) = CharacterModel(
        info = mapCharacterResponseForInfo(characterResponse.info),
        result = mapListCharacterResponseForResult(characterResponse.results)
    )


    fun mapCharacterResultForCharacterResultDb(characterResult: CharacterResult): CharacterDbModel {
        return CharacterDbModel (
            id = characterResult.id,
            name = characterResult.name,
            gender = characterResult.gender,
            status = characterResult.status,
            species = characterResult.species,
            image = characterResult.image,
            url = characterResult.url,
            type = characterResult.type,
            created =  characterResult.created,
            location = characterResult.location.name.toString(),
            origin = characterResult.origin.name.toString()
        )
    }

    fun mapCharacterResultDbForCharacterResult(characterDbModel: CharacterDbModel): CharacterResult{
        return CharacterResult(
            created = characterDbModel.created,
            id = characterDbModel.id,
            status = characterDbModel.status,
            name = characterDbModel.name,
            species = characterDbModel.species,
            image = characterDbModel.image,
            location = CharacterLocation(characterDbModel.location,""),
            url = characterDbModel.url,
            type = characterDbModel.type,
            episode = emptyList(),
            gender = characterDbModel.gender,
            origin = CharacterOrigin(characterDbModel.origin,"")
        )
    }

    fun mapListResultResponseForListDb(list: List<CharacterResult>) = list.map {
        mapCharacterResultForCharacterResultDb(it)
    }

    private fun mapOriginResponseForOrigin(characterOriginResponse: CharacterOriginResponse): CharacterOrigin {
        return CharacterOrigin(
            name = characterOriginResponse.name,
            url = characterOriginResponse.url
        )
    }
    companion object{
        private const val EMPTY_STRING = ""
        private const val ZERO_NUMBER = 0
    }

}