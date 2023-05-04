package com.example.rickyandmorty.di

import com.example.rickyandmorty.presentation.MainActivity
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterFragment
import com.example.rickyandmorty.presentation.fragments.characters.list.ListCharactersFragment
import com.example.rickyandmorty.presentation.fragments.locations.detail.DetailLocationFragment
import com.example.rickyandmorty.presentation.fragments.locations.list.ListLocationsFragment
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun injectMainActivity(mainActivity: MainActivity)
    fun injectListCharactersFragment(mainActivity: ListCharactersFragment)
    fun injectDetailCharacterFragment(mainActivity: DetailCharacterFragment)
    fun injectListLocationsFragment(mainActivity: ListLocationsFragment)
    fun injectDetailLocationFragment(mainActivity: DetailLocationFragment)


}