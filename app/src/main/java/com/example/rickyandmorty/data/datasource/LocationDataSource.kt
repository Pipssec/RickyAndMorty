package com.example.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.domain.model.locations.Locations

class LocationDataSource(
    private val name: String,
    private val type: String,
    private val dimension: String,
) : PagingSource<Int, Locations>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Locations> {
        return try {
            val response = NetworkApi.getInstance()
            val currentLoadingPageKey = params.key ?: 1
            val responseData = mutableListOf<Locations>()
            (response.getAllLocation(currentLoadingPageKey,name, type, dimension)).body()
                ?.let { responseData.addAll(it.results) }

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentLoadingPageKey == 1) null else -1,
                nextKey = currentLoadingPageKey.plus(1)
            )
        } catch (e: java.lang.Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Locations>): Int? {
        return null
    }
}