package com.example.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.CharacterApi
import com.example.rickyandmorty.domain.model.Characters

class CharacterDataSource(private val characterApi: CharacterApi) : PagingSource<Int, Characters>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Characters> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = characterApi.getAllCharacters(currentLoadingPageKey)
            val responseData = mutableListOf<Characters>()
            val data = response.body()?.results ?: emptyList()
            responseData.addAll(data)

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

    override fun getRefreshKey(state: PagingState<Int, Characters>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

}