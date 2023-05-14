package com.example.rickyandmorty.data.mappers

import com.example.rickyandmorty.data.api.response.episode.EpisodeResultResponse
import com.example.rickyandmorty.data.db.entity.episode.EpisodeDbModel
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import org.junit.Assert.assertEquals
import org.junit.Test

internal class EpisodeMapperTest{
    private val episodeMapper = EpisodeMapper()

    @Test
    fun episodeResultResponseToEpisodeResult(){
        val episodeResultResponse = EpisodeResultResponse(
            id = EPISODE_ID,
            name = EPISODE_NAME,
            air_date = EPISODE_AIR_DATE,
            episode = EPISODE_EPISODE,
            characters = CHARACTER_CHARACTERS,
            url = EPISODE_URL,
            created = EPISODE_CREATED,
        )

        val expectedEpisodeResult = EpisodeResult(
            id = EPISODE_ID,
            name = EPISODE_NAME,
            air_date = EPISODE_AIR_DATE,
            episode = EPISODE_EPISODE,
            characters = CHARACTER_CHARACTERS,
            url = EPISODE_URL,
            created = EPISODE_CREATED,
        )

        val actualEpisodeResult : EpisodeResult = episodeMapper.mapResultsResponseForResults(episodeResultResponse)

        assertEquals(expectedEpisodeResult, actualEpisodeResult)

    }

    @Test
    fun episodeResultToEpisodeResultDb() {

        val episodeResult = EpisodeResult(
            id = EPISODE_ID,
            name = EPISODE_NAME,
            air_date = EPISODE_AIR_DATE,
            episode = EPISODE_EPISODE,
            characters = CHARACTER_CHARACTERS,
            url = EPISODE_URL,
            created = EPISODE_CREATED,
        )

        val expectedEpisodeDbModel = EpisodeDbModel(
            id = EPISODE_ID,
            name = EPISODE_NAME,
            air_date = EPISODE_AIR_DATE,
            episode = EPISODE_EPISODE,
            url = EPISODE_URL,
            created = EPISODE_CREATED,
        )

        val actualEpisodeDbModel: EpisodeDbModel = episodeMapper.mapEpisodeResultForEpisodeResultDb(episodeResult)

        assertEquals(expectedEpisodeDbModel, actualEpisodeDbModel)
    }

    @Test
    fun episodeResultDbToEpisodeResult() {

        val episodeDbModel = EpisodeDbModel(
            id = EPISODE_ID,
            name = EPISODE_NAME,
            air_date = EPISODE_AIR_DATE,
            episode = EPISODE_EPISODE,
            url = EPISODE_URL,
            created = EPISODE_CREATED,
        )

        val expectedEpisodeResult = EpisodeResult(
            id = EPISODE_ID,
            name = EPISODE_NAME,
            air_date = EPISODE_AIR_DATE,
            episode = EPISODE_EPISODE,
            characters = emptyList(),
            url = EPISODE_URL,
            created = EPISODE_CREATED,
        )

        val actualEpisodeResult: EpisodeResult = episodeMapper.mapEpisodeResultDbForEpisodeResult(episodeDbModel)

        assertEquals(expectedEpisodeResult, actualEpisodeResult)

    }

    companion object {
        private const val EPISODE_ID = 123
        private const val EPISODE_NAME = "some name"
        private const val EPISODE_AIR_DATE = "some air date"
        private const val EPISODE_EPISODE = "some episode"
        private val CHARACTER_CHARACTERS = listOf(
            "https://test.com/api/character/1",
            "https://test.com/api/character/2",
            "https://test.com/api/character/3",
        )
        private const val EPISODE_URL = "some url"
        private const val EPISODE_CREATED = "some created"
    }

}