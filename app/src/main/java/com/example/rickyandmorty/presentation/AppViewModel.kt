package com.example.rickyandmorty.presentation


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.rickyandmorty.data.datasource.CharacterDataSource
import com.example.rickyandmorty.domain.model.Characters
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn



class AppViewModel() : ViewModel() {
    val errorMessage = MutableLiveData<String>()
    private val selectedItemCharacter = MutableLiveData<Characters>()

    var characterFlow: Flow<PagingData<Characters>> = emptyFlow()

    fun load(name: String, status: String, gender: String) {
        characterFlow = Pager(PagingConfig(pageSize = 1)) {
            CharacterDataSource(name, status, gender)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

    fun onClickItemCharacter(character: Characters) {
        selectedItemCharacter.value = character
    }
}



