package com.example.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.CharacterApi
import com.example.rickyandmorty.data.api.CharacterApi.Companion.characterApi
import com.example.rickyandmorty.domain.model.Characters

class CharacterDataSource(
                          private val name: String,
                          private val status: String,
                          private val gender: String
) : PagingSource<Int, Characters>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Characters> {
        try {
            val currentLoadingPageKey = params.key ?: 1
            val response = CharacterApi.getInstance()
            val responseData = mutableListOf<Characters>()
//            val data = response.body()?.results ?: emptyList()
            (response.getAllCharacters(currentLoadingPageKey,name, status, gender)).body()
                ?.let { responseData.addAll(it.results) }

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