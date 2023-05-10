package com.example.rickyandmorty.presentation.fragments.episodes.list

import android.content.Context
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
import androidx.paging.PagingData
import com.example.rickyandmorty.R
import com.example.rickyandmorty.app.App
import com.example.rickyandmorty.data.api.exception.BackendException
import com.example.rickyandmorty.databinding.FragmentEpisodeFilterBinding
import com.example.rickyandmorty.databinding.FragmentEpisodesListBinding
import com.example.rickyandmorty.di.ViewModelFactory
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import com.example.rickyandmorty.presentation.adapters.episodes.EpisodesPagingAdapter
import com.example.rickyandmorty.presentation.fragments.episodes.EpisodesViewModel
import com.example.rickyandmorty.presentation.fragments.episodes.detail.DetailEpisodesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListEpisodesFragment : Fragment(), EpisodesPagingAdapter.EpisodeListener {
    private lateinit var binding: FragmentEpisodesListBinding
    private lateinit var bindingFilter: FragmentEpisodeFilterBinding
    private lateinit var episodesViewModel: EpisodesViewModel
    private var adapter = EpisodesPagingAdapter(this)
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private var name = ""
    private var episode = ""

    override fun onAttach(context: Context) {
        (requireActivity().application as App).appComponent.injectListEpisodesFragment(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFilter = FragmentEpisodeFilterBinding.inflate(inflater)
        episodesViewModel = ViewModelProvider(requireActivity(), viewModelFactory)[EpisodesViewModel::class.java]
        binding = FragmentEpisodesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAllEpisodes(name)
        initProgressBar()
        findByName()
        initEpisodesFilter()
        showBotNav()
        swipeRefresh()
    }

    private fun swipeRefresh(){
        binding.swipeRefreshEpisodes.setOnRefreshListener {
            lifecycleScope.launch{
                adapter.submitData(PagingData.empty())
                episodesViewModel.episodeFlow.collectLatest(adapter::submitData)
            }
            binding.swipeRefreshEpisodes.isRefreshing = false
        }
    }

    private fun findByName() {
        binding.searchInListEpisodes.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val nameText = query.toString()
                name = nameText
                loadAllEpisodes(name)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val nameText = newText.toString()
                name = nameText
                loadAllEpisodes(name)
                return true
            }
        })
    }

    private fun initEpisodesFilter() {
        binding.btnEpisodesFilter.setOnClickListener {
            openEpisodesFilter()
        }
    }

    private fun openEpisodesFilter() = with(bindingFilter) {
        val dialog = BottomSheetDialog(requireContext())
        val seasonsArray = arrayOf("S01", "S02", "S03", "S04", "S05")
        val episodeArray =
            arrayOf("E01", "E02", "E03", "E04", "E05", "E06", "E07", "E08", "E09", "E10", "E11")
        var selectSeason = ""
        var selectEpisode = ""
        val seasonsAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            seasonsArray
        )
        val episodeAdapter = ArrayAdapter<String>(
            requireActivity(),
            android.R.layout.simple_spinner_dropdown_item,
            episodeArray
        )
        spinnerSeason.adapter = seasonsAdapter

        spinnerSeason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        spinnerEpisode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

        if (bindingFilter.root.parent != null) {
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
            episodesViewModel.episodeFlow.collectLatest(adapter::submitData)
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

    override fun onClick(episode: EpisodeResult) {
        episodesViewModel.onClickItemEpisodes(episode)
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.containerFragment, DetailEpisodesFragment())
            ?.addToBackStack(null)
            ?.commit()

    }

    private fun showBotNav() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }
}