package com.example.rickyandmorty.presentation.fragments.locations.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickyandmorty.data.api.exception.BackendException
import com.example.rickyandmorty.data.api.exception.NoDataException
import com.example.rickyandmorty.databinding.FragmentLocationsListBinding
import com.example.rickyandmorty.domain.model.locations.Locations
import com.example.rickyandmorty.presentation.adapters.location.LocationsPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListLocationsFragment: Fragment(), LocationsPagingAdapter.Listener {
    private lateinit var binding: FragmentLocationsListBinding
    private var adapter = LocationsPagingAdapter(this)
    private  lateinit var listLocationsViewModel : ListLocationsViewModel
    private var name = ""
    private var type = ""
    private var dimension = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationsListBinding.inflate(inflater, container, false)
        listLocationsViewModel = ViewModelProvider(this)[ListLocationsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvListLocations.adapter = adapter
        loadAllLocations()
        initProgressBar()
        findByName()
    }

    private fun loadAllLocations(){
        listLocationsViewModel.errorMessage.observe(requireActivity()) {
            Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            listLocationsViewModel.loadLocations(name, type, dimension)
            listLocationsViewModel.locationFlow.collectLatest(adapter::submitData)
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

    private fun findByName() {
        binding.rvListLocations.adapter = adapter
        binding.searchInListLocations.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val name = query.toString()
                lifecycleScope.launch {
                    listLocationsViewModel.loadLocations(name, type, dimension)
                    listLocationsViewModel.locationFlow.collectLatest(adapter::submitData)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val name = newText.toString()
                lifecycleScope.launch {
                    listLocationsViewModel.loadLocations(name, type, dimension)
                    listLocationsViewModel.locationFlow.collectLatest(adapter::submitData)
                }
                return true
            }
        })
    }

    override fun onClick(location: Locations) {
        TODO("Not yet implemented")
    }
}