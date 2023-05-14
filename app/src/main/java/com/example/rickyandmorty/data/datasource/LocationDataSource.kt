package com.example.rickyandmorty.data.datasource

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.ext.backendException
import com.example.rickyandmorty.domain.models.locations.LocationResult
import com.example.rickyandmorty.domain.repository.LocationRepository
import javax.inject.Inject

class LocationDataSource @Inject constructor(
    private val repository: LocationRepository,
    private val application: Application,
    private val name: String,
    private val type: String,
    private val dimension: String,
) : PagingSource<Int, LocationResult>() {
    companion object {
        private const val START_PAGE = 1
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LocationResult> {
        return try {
            var nextKey: Int? = 0
            val page = params.key ?: START_PAGE
            val responseData = if (hasConnected(application.applicationContext)) {
                val response = repository.getLocation(page, name, type, dimension)
                nextKey = if (response.info.next == null) null else page + 1
                response.results
            } else {
                val listLocations = repository.getListLocationsDb(
                    (page - 1) * params.loadSize,
                    params.loadSize,
                    name,
                    type,
                    dimension
                )
                nextKey = if (listLocations.isNotEmpty()) page + 1 else null
                listLocations
            }
            val prevKey = if (page == START_PAGE) null else page - 1
            LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(backendException(e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LocationResult>): Int? = null

    private fun hasConnected(context: Context): Boolean {
        val manager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetworkInfo
        return network != null && network.isConnected
    }
}
