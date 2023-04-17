package com.example.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.domain.model.characters.Characters

class CharacterDataSource(
                          private val name: String,
                          private val status: String,
                          private val gender: String
) : PagingSource<Int, Characters>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Characters> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = NetworkApi.getInstance()
            val responseData = mutableListOf<Characters>()
            (response.getAllCharacters(currentLoadingPageKey,name, status, gender)).body()
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

    override fun getRefreshKey(state: PagingState<Int, Characters>): Int? {
        return null
    }
}