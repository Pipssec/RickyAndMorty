package com.example.rickyandmorty.di

import androidx.lifecycle.ViewModel
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterViewModel
import com.example.rickyandmorty.presentation.fragments.characters.list.ListCharactersViewModel
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


    companion object{
        @Provides
        fun provideApiService(): NetworkApi {
            return NetworkApi.getInstance()
        }
    }
}