package com.example.rickyandmorty.presentation.fragments.episodes.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import com.example.rickyandmorty.R
import com.example.rickyandmorty.databinding.FragmentEpisodeDetailBinding
import com.example.rickyandmorty.domain.model.characters.Characters
import com.example.rickyandmorty.presentation.adapters.location.detail.DetailLocationCharacterAdapter
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterFragment
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterViewModel
import com.example.rickyandmorty.presentation.fragments.episodes.EpisodesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class DetailEpisodesFragment(private val episodesViewModel: EpisodesViewModel) : Fragment(), DetailLocationCharacterAdapter.SelectListener {
    private lateinit var binding: FragmentEpisodeDetailBinding
//    private val episodesViewModel: EpisodesViewModel by activityViewModels()
    private val detailCharacterViewModel: DetailCharacterViewModel by activityViewModels()
    lateinit var adapter : DetailLocationCharacterAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBotNav()
        episodesViewModel.selectedItemLocation.observe(viewLifecycleOwner) {
            binding.tvNameEpisodeDetail.text = it.name
            binding.tvEpisodeDetail.text = it.episode
            binding.tvDataEpisodeDetail.text = it.air_date
        }
        binding.btnBack.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
        episodesViewModel.getCharacters()
        episodesViewModel.fetchData()
        episodesViewModel.responseCharacters.observe(viewLifecycleOwner){
            adapter = DetailLocationCharacterAdapter(requireContext(), it, this)
            binding.rvDetailEpisodeCharacters.adapter = adapter
        }

    }

    override fun onStop() {
        super.onStop()
        episodesViewModel.clearListCharacters()
    }

    private fun hideBotNav() {
        val botNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        botNav.visibility = View.GONE
    }

    override fun onItemClicked(character: Characters?) {
        detailCharacterViewModel.onClickItemCharacter(character)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(
                R.id.containerFragment,
                DetailCharacterFragment()
            )
            .addToBackStack(null)
            .commit()
    }
}

