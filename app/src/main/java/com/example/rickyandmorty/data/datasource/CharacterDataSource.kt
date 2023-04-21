package com.example.rickyandmorty.data.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.data.api.exception.NoDataException
import com.example.rickyandmorty.data.api.ext.backendException
import com.example.rickyandmorty.domain.model.characters.Characters

class CharacterDataSource(
                          private val name: String,
                          private val status: String,
                          private val gender: String,
                          private val species: String
) : PagingSource<Int, Characters>() {

    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Characters> {
        try {
            val page = params.key ?: START_PAGE
            val networkApi = NetworkApi.getInstance()
            val response = networkApi.getAllCharacters(page,name, status, gender, species)
            val characters = response.results
            if(characters.isEmpty()){
                return LoadResult.Error(NoDataException())
            }

            val prevKey = if (page == START_PAGE) null else page - 1
            val nextKey = if (response.info.next == null) null else page+1
            return LoadResult.Page(
                data = characters,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(backendException(e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Characters>): Int? = null
}