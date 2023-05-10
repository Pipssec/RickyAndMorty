package com.example.rickyandmorty.domain.usecases.character

import android.app.Application
import com.example.rickyandmorty.data.datasource.CharacterDataSource
import com.example.rickyandmorty.data.repository.CharacterRepositoryImpl
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import io.reactivex.Observable
import javax.inject.Inject

class CharacterUseCase @Inject constructor(
    private val repository: CharacterRepositoryImpl,
    private val application: Application
) {
    fun getCharacter(name: String, status: String, gender: String, species: String): CharacterDataSource {
        return CharacterDataSource(repository,application,name, status,gender, species)
    }

    fun getDetailEpisode(id: String): Observable<List<EpisodeResult>>{
        return repository.getDetailEpisode(id)
    }
}