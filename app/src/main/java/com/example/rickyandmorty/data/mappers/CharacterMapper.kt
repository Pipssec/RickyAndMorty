package com.example.rickyandmorty.data.mappers


import com.example.rickyandmorty.data.api.response.character.*
import com.example.rickyandmorty.data.db.entity.character.CharacterDbModel
import com.example.rickyandmorty.domain.models.character.*
import javax.inject.Inject

class CharacterMapper @Inject constructor() {
     private fun mapCharacterResponseForInfo(characterResponseInfo: CharacterInfoResponse? ) = CharacterInfo(
            pages = characterResponseInfo?.pages ?: ZERO_NUMBER,
            next = characterResponseInfo?.next ?: EMPTY_STRING,
            count = characterResponseInfo?.count ?: ZERO_NUMBER,
            prev = characterResponseInfo?.prev ?: EMPTY_STRING
    )

    private fun mapLocationResponseForLocation(locationResponse: CharacterLocationResponse?) = CharacterLocation(
        name = locationResponse?.name ?: EMPTY_STRING,
        url = locationResponse?.url ?: EMPTY_STRING
    )

    private fun mapResultResponseForResult(resultResponse: CharacterResultResponse?) = CharacterResult(
        id = resultResponse?.id ?: ZERO_NUMBER,
        created = resultResponse?.created ?: EMPTY_STRING,
        episode = resultResponse?.episode ?: emptyList(),
        gender = resultResponse?.gender ?: EMPTY_STRING,
        image = resultResponse?.image ?: EMPTY_STRING,
        name = resultResponse?.name ?: EMPTY_STRING,
        species = resultResponse?.species ?: EMPTY_STRING,
        status = resultResponse?.status ?: EMPTY_STRING,
        type = resultResponse?.type ?: EMPTY_STRING,
        url = resultResponse?.url ?: EMPTY_STRING,
        location = mapLocationResponseForLocation(resultResponse?.location),
        origin = mapOriginResponseForOrigin(resultResponse?.origin!!)
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

    fun mapCharacterResultDbForCharacterResult(characterResult: CharacterDbModel): CharacterResult{
        return CharacterResult(
            created = characterResult.created,
            id = characterResult.id,
            status = characterResult.status,
            name = characterResult.name,
            species = characterResult.species,
            image = characterResult.image,
            location = CharacterLocation(characterResult.location,""),
            url = characterResult.url,
            type = characterResult.type,
            episode = emptyList(),
            gender = characterResult.gender,
            origin = CharacterOrigin(characterResult.origin,"")
        )
    }

    fun mapListResultResponseForListDb(list: List<CharacterResult>) = list.map {
        mapCharacterResultForCharacterResultDb(it)
    }

    private fun mapOriginResponseForOrigin(originDto: CharacterOriginResponse): CharacterOrigin {
        return CharacterOrigin(
            name = originDto.name,
            url = originDto.url
        )
    }
    companion object{
        private const val EMPTY_STRING = ""
        private const val ZERO_NUMBER = 0
    }

}