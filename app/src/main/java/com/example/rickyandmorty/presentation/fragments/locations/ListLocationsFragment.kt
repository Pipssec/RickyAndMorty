package com.example.rickyandmorty.presentation.fragments.locations

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
import com.example.rickyandmorty.databinding.FragmentLocationsListBinding
import com.example.rickyandmorty.domain.model.locations.Locations
import com.example.rickyandmorty.presentation.adapters.LocationsPagingAdapter
import com.example.rickyandmorty.presentation.fragments.locations.viewmodels.ListLocationsViewModel
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
    }

    private fun loadAllLocations(){
        listLocationsViewModel.errorMessage.observe(requireActivity()) {
            Toast.makeText(requireContext(), "Error1", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            listLocationsViewModel.loadLocations(name, type, dimension)
            listLocationsViewModel.locationFlow.collectLatest(adapter::submitData)
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

    override fun onClick(location: Locations) {
        TODO("Not yet implemented")
    }
}