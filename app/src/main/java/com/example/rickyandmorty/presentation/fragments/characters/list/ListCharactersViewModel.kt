package com.example.rickyandmorty.presentation.fragments.characters.list


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.usecases.character.CharacterUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


class ListCharactersViewModel @Inject constructor(
    private val characterUseCase: CharacterUseCase
) : ViewModel() {
    val errorMessage = MutableLiveData<String>()

    var characterFlow: Flow<PagingData<CharacterResult>> = emptyFlow()

    fun loadCharacters(name: String, status: String, gender: String, species: String) {
        characterFlow =
            Pager(PagingConfig(pageSize = 20, enablePlaceholders = false, initialLoadSize = 20)) {
                characterUseCase.getCharacter(name, status, gender, species)
            }.flow.cachedIn(viewModelScope)
                .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())

    }

}



