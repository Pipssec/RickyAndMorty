package com.example.rickyandmorty.data.datasource

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.ext.backendException
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import com.example.rickyandmorty.domain.repository.EpisodeRepository
import javax.inject.Inject


class EpisodesDataSource@Inject constructor(
    private val repository: EpisodeRepository,
    private val application: Application,
    private val name: String,
    private val episode: String
) : PagingSource<Int, EpisodeResult>() {

    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodeResult> {
        try {
            var nextKey: Int? = 0
            val page = params.key ?: START_PAGE
            val responseData =  if(hasConnected(application.applicationContext)){
                val response = repository.getEpisode(page,name,episode)
                nextKey = if(response.info.next == null) null else page + 1
                response.results
            } else {
                val listLocations = repository.getListEpisodesDb((page-1) * params.loadSize, params.loadSize, name, episode)
                nextKey = if(listLocations.isNotEmpty()) page +1  else null
                listLocations
            }

            val prevKey = if (page == START_PAGE) null else page - 1
            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(backendException(e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodeResult>): Int? = null

    private fun hasConnected(context: Context): Boolean{
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetworkInfo
        return network != null && network.isConnected
    }
}
