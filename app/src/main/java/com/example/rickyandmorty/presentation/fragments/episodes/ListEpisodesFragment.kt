package com.example.rickyandmorty.presentation.fragments.episodes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickyandmorty.databinding.FragmentEpisodesListBinding
import com.example.rickyandmorty.domain.model.episodes.Episodes
import com.example.rickyandmorty.presentation.adapters.episodes.EpisodesPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListEpisodesFragment: Fragment(), EpisodesPagingAdapter.Listener {
    private lateinit var binding: FragmentEpisodesListBinding
    private lateinit var episodesViewModel: EpisodesViewModel
    private var adapter = EpisodesPagingAdapter(this)
    private var name = ""
    private var episode = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEpisodesListBinding.inflate(inflater, container, false)
        episodesViewModel = ViewModelProvider(this)[EpisodesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvListEpisodes.adapter = adapter
        loadAllEpisodes()
        initProgressBar()


    }

    private fun loadAllEpisodes(){
       episodesViewModel.errorMessage.observe(requireActivity()) {
            Toast.makeText(requireContext(), "Error1", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            episodesViewModel.loadAllEpisodes(name, episode)
            episodesViewModel.episodesFlow.collectLatest(adapter::submitData)
        }
    }

    private fun initProgressBar(){
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading)
                binding.progressDialog.isVisible = true
            else {
                binding.progressDialog.isVisible = false

                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error ->  loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Log.d("resiult", it.toString())
                    Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onClick(episode: Episodes) {
        TODO("Not yet implemented")
    }

}