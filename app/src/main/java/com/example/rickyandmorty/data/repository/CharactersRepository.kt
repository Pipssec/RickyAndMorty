package com.example.rickyandmorty.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.rickyandmorty.data.api.CharacterApi
import com.example.rickyandmorty.data.datasource.CharacterDataSource
import com.example.rickyandmorty.domain.model.Characters

class CharactersRepository(private val characterApi: CharacterApi) {
    fun getAllCharacters(): LiveData<PagingData<Characters>>{
        return Pager(
            config = PagingConfig(
                pageSize = 42,
                enablePlaceholders = false,
                initialLoadSize = 2
            ),
            pagingSourceFactory = {
                CharacterDataSource(characterApi)
            }
            , initialKey = 1
        ).liveData
    }
}