package com.example.rickyandmorty.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.rickyandmorty.R
import com.example.rickyandmorty.databinding.ActivityMainBinding
import com.example.rickyandmorty.presentation.fragments.characters.list.ListCharactersFragment
import com.example.rickyandmorty.presentation.fragments.episodes.ListEpisodesFragment
import com.example.rickyandmorty.presentation.fragments.locations.list.ListLocationsFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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