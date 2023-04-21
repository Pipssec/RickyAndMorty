package com.example.rickyandmorty.presentation.fragments.characters.list


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.rickyandmorty.data.datasource.CharacterDataSource
import com.example.rickyandmorty.domain.model.characters.Characters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn



class ListCharactersViewModel() : ViewModel() {
    val errorMessage = MutableLiveData<String>()

    var characterFlow: Flow<PagingData<Characters>> = emptyFlow()

    fun loadCharacters(name: String, status: String, gender: String, species: String) {
        characterFlow = Pager(PagingConfig(pageSize = 1)) {
            CharacterDataSource(name, status, gender, species)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

}



