package com.example.rickyandmorty.di

import androidx.lifecycle.ViewModel
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterViewModel
import com.example.rickyandmorty.presentation.fragments.characters.list.ListCharactersViewModel
import com.example.rickyandmorty.presentation.fragments.locations.detail.DetailLocationViewModel
import com.example.rickyandmorty.presentation.fragments.locations.list.ListLocationsViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
interface AppModule {

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

    companion object{
        @Provides
        fun provideApiService(): NetworkApi {
            return NetworkApi.getInstance()
        }
    }
}