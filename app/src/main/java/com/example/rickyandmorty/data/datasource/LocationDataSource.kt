package com.example.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.data.api.ext.backendException
import com.example.rickyandmorty.domain.models.locations.Location

class LocationDataSource(
    private val name: String,
    private val type: String,
    private val dimension: String,
) : PagingSource<Int, Location>() {
    companion object{
        private const val START_PAGE = 1
    }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Location> {
        return try {
            val page = params.key ?: START_PAGE
            val networkApi = NetworkApi.getInstance()
            val response = networkApi.getAllLocation(page,name, type, dimension)
            val locations = response.results

            val prevKey = if (page == START_PAGE) null else page - 1
            val nextKey = if (response.info.next == null) null else page+1

            LoadResult.Page(
                data = locations,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: java.lang.Exception) {
            LoadResult.Error(backendException(e))
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Location>): Int? {
        return null
    }
}