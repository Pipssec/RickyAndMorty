package com.example.rickyandmorty.presentation.fragments.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickyandmorty.data.api.exception.BackendException
import com.example.rickyandmorty.data.api.exception.NoDataException
import com.example.rickyandmorty.databinding.FragmentEpisodeFilterBinding
import com.example.rickyandmorty.databinding.FragmentEpisodesListBinding
import com.example.rickyandmorty.domain.model.episodes.Episodes
import com.example.rickyandmorty.presentation.adapters.episodes.EpisodesPagingAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListEpisodesFragment : Fragment(), EpisodesPagingAdapter.Listener {
    private lateinit var binding: FragmentEpisodesListBinding
    private lateinit var bindingFilter: FragmentEpisodeFilterBinding
    private lateinit var episodesViewModel: EpisodesViewModel
    private var adapter = EpisodesPagingAdapter(this)
    private var name = ""
    private var episode = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFilter = FragmentEpisodeFilterBinding.inflate(inflater)
        binding = FragmentEpisodesListBinding.inflate(inflater, container, false)
        episodesViewModel = ViewModelProvider(this)[EpisodesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEpisodesFilter()
        loadAllEpisodes(name)
        initProgressBar()
        findByName()



    }

    private fun findByName() {
        binding.searchInListEpisodes.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val name = query.toString()
                loadAllEpisodes(name)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val name = newText.toString()
                loadAllEpisodes(name)
                return true
            }
        })
    }

    private fun initEpisodesFilter(){
        binding.btnEpisodesFilter.setOnClickListener{
            openEpisodesFilter()
        }
    }
    private fun openEpisodesFilter() = with(bindingFilter) {
        val dialog = BottomSheetDialog(requireContext())
        val seasonsArray = arrayOf("S01", "S02","S03", "S04", "S05")
        val episodeArray = arrayOf("E01", "E02", "E03","E04","E05","E06","E07","E08","E09","E10","E11")
        var selectSeason = ""
        var selectEpisode = ""
        val seasonsAdapter = ArrayAdapter<String>(requireActivity(),android.R.layout.simple_spinner_dropdown_item,seasonsArray)
        val episodeAdapter = ArrayAdapter<String>(requireActivity(),android.R.layout.simple_spinner_dropdown_item,episodeArray)
        spinnerSeason.adapter = seasonsAdapter

        spinnerSeason.onItemSelectedListener = object  : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectSeason = seasonsArray[position]
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        spinnerEpisode.adapter = episodeAdapter
        spinnerEpisode.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectEpisode = episodeArray[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}

        }

        if(bindingFilter.root.parent != null){
            (bindingFilter.root.parent as ViewGroup).removeView(bindingFilter.root)
        }
        dialog.setContentView(bindingFilter.root)
        dialog.show()
        btnCloseEpisodeDialog.setOnClickListener {
            dialog.dismiss()
        }
        btnApply.setOnClickListener {
            episode = selectSeason + selectEpisode
            loadAllEpisodes(name)
            dialog.dismiss()
        }
        btnResetFilter.setOnClickListener {
            episode = ""
            loadAllEpisodes(name)
            dialog.dismiss()
        }
    }

    private fun loadAllEpisodes(name: String) {
        binding.rvListEpisodes.adapter = adapter
        episodesViewModel.errorMessage.observe(requireActivity()) {
            Toast.makeText(requireContext(), "Error1", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            episodesViewModel.loadAllEpisodes(name, episode)
            episodesViewModel.episodesFlow.collectLatest(adapter::submitData)
        }
    }

    private fun initProgressBar() {
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                loadState.append is LoadState.Loading
            )
                binding.progressDialog.isVisible = true
            else {
                binding.progressDialog.isVisible = false

                val error = when {
                    loadState.append is LoadState.Error -> (loadState.append as LoadState.Error).error
                    loadState.refresh is LoadState.Error -> (loadState.refresh as LoadState.Error).error
                    else -> null
                }
                when (error) {
                    null -> {}
                    is NoDataException -> Toast.makeText(
                        requireContext(),
                        "Данные не найдены",
                        Toast.LENGTH_LONG
                    ).show()
                    is BackendException -> Toast.makeText(
                        requireContext(),
                        "Данные не найдены",
                        Toast.LENGTH_LONG
                    ).show()
                    else -> Toast.makeText(
                        requireContext(),
                        "Неизвестная ошибка",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onClick(episode: Episodes) {
        TODO("Not yet implemented")
    }

}