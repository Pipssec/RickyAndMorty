package com.example.rickyandmorty.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickyandmorty.data.repository.CharactersRepository
import com.example.rickyandmorty.domain.model.Characters


class AppViewModel(private val charactersRepository: CharactersRepository) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    private val selectedItemCharacter = MutableLiveData<Characters>()

    fun getCharactersList(): LiveData<PagingData<Characters>> {
        return charactersRepository.getAllCharacters().cachedIn(viewModelScope)
    }

    fun onClickItemCharacter(character:Characters){
        selectedItemCharacter.value = character
    }

}
