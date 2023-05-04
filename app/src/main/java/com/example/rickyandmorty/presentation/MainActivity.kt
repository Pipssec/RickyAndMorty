package com.example.rickyandmorty.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.rickyandmorty.R
import com.example.rickyandmorty.app.App
import com.example.rickyandmorty.databinding.ActivityMainBinding
import com.example.rickyandmorty.presentation.fragments.characters.list.ListCharactersFragment
import com.example.rickyandmorty.presentation.fragments.episodes.list.ListEpisodesFragment
import com.example.rickyandmorty.presentation.fragments.locations.list.ListLocationsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_RickyAndMorty)
        super.onCreate(savedInstanceState)
        (application as App).appComponent.injectMainActivity(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initialBotNav()

    }

    private fun initialBotNav() {
        replaceFragment(ListCharactersFragment())
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.characters -> replaceFragment(ListCharactersFragment())
                R.id.locations -> replaceFragment(ListLocationsFragment())
                R.id.episodes -> replaceFragment(ListEpisodesFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.containerFragment, fragment)
            .commit()
    }
}