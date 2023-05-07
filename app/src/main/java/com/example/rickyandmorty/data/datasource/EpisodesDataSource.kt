package com.example.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.data.api.ext.backendException
import com.example.rickyandmorty.domain.models.episodes.Episode


class EpisodesDataSource(
    private val name: String,
    private val episode: String
) : PagingSource<Int, Episode>() {

    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        try {
            val page = params.key ?: START_PAGE
            val networkApi = NetworkApi.getInstance()
            val response = networkApi.getAllEpisode(page, name, episode)
            val episodes = response.results

            val prevKey = if (page == START_PAGE) null else page - 1
            val nextKey = if (response.info.next == null) null else page+1
            return LoadResult.Page(
                data = episodes,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(backendException(e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? = null
}
