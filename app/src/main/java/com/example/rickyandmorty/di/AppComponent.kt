package com.example.rickyandmorty.di

import android.app.Application
import com.example.rickyandmorty.app.App
import com.example.rickyandmorty.presentation.MainActivity
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterFragment
import com.example.rickyandmorty.presentation.fragments.characters.list.ListCharactersFragment
import com.example.rickyandmorty.presentation.fragments.episodes.detail.DetailEpisodesFragment
import com.example.rickyandmorty.presentation.fragments.episodes.list.ListEpisodesFragment
import com.example.rickyandmorty.presentation.fragments.locations.detail.DetailLocationFragment
import com.example.rickyandmorty.presentation.fragments.locations.list.ListLocationsFragment
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectMainActivity(mainActivity: MainActivity)
    fun injectListCharactersFragment(mainActivity: ListCharactersFragment)
    fun injectDetailCharacterFragment(mainActivity: DetailCharacterFragment)
    fun injectListLocationsFragment(mainActivity: ListLocationsFragment)
    fun injectDetailLocationFragment(mainActivity: DetailLocationFragment)
    fun injectDetailEpisodesFragment(mainActivity: DetailEpisodesFragment)
    fun injectListEpisodesFragment(mainActivity: ListEpisodesFragment)
    fun inject(application: App)

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application
        ): AppComponent
    }


}