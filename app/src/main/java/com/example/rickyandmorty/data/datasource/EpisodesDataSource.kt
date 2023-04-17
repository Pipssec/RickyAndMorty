package com.example.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.domain.model.episodes.Episodes

class EpisodesDataSource(
        private val name: String,
        private val episode: String
) : PagingSource<Int, Episodes>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episodes> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = NetworkApi.getInstance()
            val responseData = mutableListOf<Episodes>()
            (response.getAllEpisode(currentLoadingPageKey,name, episode)).body()
                ?.let { responseData.addAll(it.results) }
            if(responseData.size == 0){
                val exception: Exception = TypeCastException("Item not found")
                return LoadResult.Error(exception)
            }

            val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1

            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Episodes>): Int? {
        return null
    }
}