package com.example.rickyandmorty

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickyandmorty.data.repository.CharactersRepository
import com.example.rickyandmorty.presentation.AppViewModel

class MyViewModelFactory constructor(private val repository: CharactersRepository): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            AppViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}