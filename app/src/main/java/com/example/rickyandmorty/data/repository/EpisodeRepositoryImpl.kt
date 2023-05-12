package com.example.rickyandmorty.data.repository


import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.data.db.dao.EpisodeDao
import com.example.rickyandmorty.data.mappers.EpisodeMapper
import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.models.episodes.Episode
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import com.example.rickyandmorty.domain.repository.EpisodeRepository
import io.reactivex.Observable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodeRepositoryImpl @Inject constructor(
    private val apiService: NetworkApi,
    private val episodeDao: EpisodeDao,
    private val mapper: EpisodeMapper
): EpisodeRepository {

    override suspend fun getEpisode(page: Int, name: String, episode: String): Episode {
        val episodeApi = apiService.getAllEpisode(page,name,episode)
        val listEpisodes = mapper.mapEpisodeResponseForEpisode(episodeApi)
        episodeDao.insertEpisode(mapper.mapListResultResponseForListDb(listEpisodes.results))
        return listEpisodes
    }

    override suspend fun insertEpisode(list: List<EpisodeResult>) {
        episodeDao.insertEpisode(mapper.mapListResultResponseForListDb(list))
    }

    override suspend fun getListEpisodesDb(offset: Int,
                                   limit: Int,
                                   name: String,
                                   episode: String
    ): List<EpisodeResult> {
        return episodeDao.getAllEpisodesPage(offset, limit, name, episode).map(mapper::mapEpisodeResultDbForEpisodeResult)
    }

    override fun getDetailCharacter(id: String): Observable<List<CharacterResult>> {
        return apiService.getDetailCharacter(id)
    }
}