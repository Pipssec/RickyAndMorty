package com.example.rickyandmorty.domain.usecases.episode

import android.app.Application
import com.example.rickyandmorty.data.datasource.EpisodesDataSource
import com.example.rickyandmorty.data.repository.EpisodeRepositoryImpl
import com.example.rickyandmorty.domain.models.character.CharacterResult
import io.reactivex.Observable
import javax.inject.Inject

class EpisodeUseCase @Inject constructor(
    private val repository: EpisodeRepositoryImpl,
    private val application: Application
) {
    fun getEpisodes(name: String, episode: String): EpisodesDataSource {
        return EpisodesDataSource(repository, application, name, episode)
    }

    fun getDetailCharacter(id: String): Observable<List<CharacterResult>> {
        return repository.getDetailCharacter(id)
    }
}