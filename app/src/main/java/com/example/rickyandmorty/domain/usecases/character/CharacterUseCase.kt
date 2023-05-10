package com.example.rickyandmorty.domain.usecases.character

import android.app.Application
import com.example.rickyandmorty.data.datasource.CharacterDataSource
import com.example.rickyandmorty.data.repository.CharacterRepositoryImpl
import javax.inject.Inject

class CharacterUseCase @Inject constructor(
    private val repository: CharacterRepositoryImpl,
    private val application: Application
) {
    fun getCharacter(name: String, status: String, gender: String, species: String): CharacterDataSource {
        return CharacterDataSource(repository,application,name, status,gender, species)
    }
}