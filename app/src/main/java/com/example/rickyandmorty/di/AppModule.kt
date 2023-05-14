package com.example.rickyandmorty.di

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.data.db.CharacterDatabase
import com.example.rickyandmorty.data.db.EpisodeDatabase
import com.example.rickyandmorty.data.db.LocationDatabase
import com.example.rickyandmorty.data.db.dao.CharacterDao
import com.example.rickyandmorty.data.db.dao.EpisodeDao
import com.example.rickyandmorty.data.db.dao.LocationDao
import com.example.rickyandmorty.data.repository.CharacterRepositoryImpl
import com.example.rickyandmorty.data.repository.EpisodeRepositoryImpl
import com.example.rickyandmorty.data.repository.LocationRepositoryImpl
import com.example.rickyandmorty.domain.repository.CharacterRepository
import com.example.rickyandmorty.domain.repository.EpisodeRepository
import com.example.rickyandmorty.domain.repository.LocationRepository
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterViewModel
import com.example.rickyandmorty.presentation.fragments.characters.list.ListCharactersViewModel
import com.example.rickyandmorty.presentation.fragments.episodes.EpisodesViewModel
import com.example.rickyandmorty.presentation.fragments.locations.detail.DetailLocationViewModel
import com.example.rickyandmorty.presentation.fragments.locations.list.ListLocationsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface AppModule {
    @Binds
    fun bindCharacterRepository(repository: CharacterRepositoryImpl): CharacterRepository

    @Binds
    fun bindEpisodeRepository(repository: EpisodeRepositoryImpl): EpisodeRepository

    @Binds
    fun bindLocationRepository(repository: LocationRepositoryImpl): LocationRepository

    @Binds
    @IntoMap
    @ViewModelKey(DetailCharacterViewModel::class)
    fun bindDetailCharacterViewModel(viewModel: DetailCharacterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListCharactersViewModel::class)
    fun bindListCharactersViewModel(viewModel: ListCharactersViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListLocationsViewModel::class)
    fun bindListLocationsViewModel(viewModel: ListLocationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailLocationViewModel::class)
    fun bindDetailLocationViewModel(viewModel: DetailLocationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesViewModel::class)
    fun bindEpisodesViewModel(viewModel: EpisodesViewModel): ViewModel

    companion object {
        @Provides
        fun provideApiService(): NetworkApi {
            return NetworkApi.getInstance()
        }

        @Provides
        fun provideCharacterDao(application: Application): CharacterDao {
            return CharacterDatabase.getMainDatabase(application).getCharacterDao()
        }

        @Provides
        fun provideEpisodeDao(application: Application): EpisodeDao {
            return EpisodeDatabase.getMainDatabase(application).getEpisodeDao()
        }

        @Provides
        fun provideLocationDao(application: Application): LocationDao {
            return LocationDatabase.getMainDatabase(application).getLocationDao()
        }
    }
}