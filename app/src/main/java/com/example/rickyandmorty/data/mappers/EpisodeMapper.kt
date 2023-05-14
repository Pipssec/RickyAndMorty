package com.example.rickyandmorty.data.mappers


import com.example.rickyandmorty.data.api.response.episode.EpisodeInfoResponse
import com.example.rickyandmorty.data.api.response.episode.EpisodeResponse
import com.example.rickyandmorty.data.api.response.episode.EpisodeResultResponse
import com.example.rickyandmorty.data.db.entity.episode.EpisodeDbModel
import com.example.rickyandmorty.domain.models.episodes.Episode
import com.example.rickyandmorty.domain.models.episodes.EpisodeInfo
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import javax.inject.Inject

class EpisodeMapper @Inject constructor() {
    private fun mapInfoResponseForInfo(infoResponse: EpisodeInfoResponse?) = EpisodeInfo(
        count = infoResponse?.count ?: ZERO_NUMBER,
        next = infoResponse?.next ?: EMPTY_STRING,
        pages = infoResponse?.pages ?: ZERO_NUMBER,
        prev = infoResponse?.prev ?: EMPTY_STRING
    )

    fun mapEpisodeResultsResponseForEpisodeResults(resultResponse: EpisodeResultResponse?) =
        EpisodeResult(
            air_date = resultResponse?.air_date ?: EMPTY_STRING,
            characters = resultResponse?.characters ?: emptyList(),
            created = resultResponse?.created ?: EMPTY_STRING,
            episode = resultResponse?.episode ?: EMPTY_STRING,
            id = resultResponse?.id ?: ZERO_NUMBER,
            name = resultResponse?.name ?: EMPTY_STRING,
            url = resultResponse?.url ?: EMPTY_STRING
        )

    private fun mapListResultsResponseForListResults(list: List<EpisodeResultResponse>) = list.map {
        mapEpisodeResultsResponseForEpisodeResults(it)
    }

    fun mapEpisodeResponseForEpisode(episodeResponse: EpisodeResponse) = Episode(
        info = mapInfoResponseForInfo(episodeResponse.info),
        results = mapListResultsResponseForListResults(episodeResponse.results)
    )

    fun mapEpisodeResultForEpisodeResultDb(episodeResult: EpisodeResult): EpisodeDbModel {
        return EpisodeDbModel(
            air_date = episodeResult.air_date,
            created = episodeResult.created,
            episode = episodeResult.episode,
            id = episodeResult.id,
            name = episodeResult.name,
            url = episodeResult.url
        )
    }

    fun mapListResultResponseForListDb(list: List<EpisodeResult>) = list.map {
        mapEpisodeResultForEpisodeResultDb(it)
    }

    fun mapEpisodeResultDbForEpisodeResult(episodeDbModel: EpisodeDbModel): EpisodeResult {
        return EpisodeResult(
            air_date = episodeDbModel.air_date,
            characters = emptyList(),
            episode = episodeDbModel.episode,
            created = episodeDbModel.created,
            id = episodeDbModel.id,
            name = episodeDbModel.name,
            url = episodeDbModel.url
        )
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val ZERO_NUMBER = 0
    }
}