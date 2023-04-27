package com.example.rickyandmorty.presentation.fragments.locations.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickyandmorty.R
import com.example.rickyandmorty.data.api.exception.BackendException
import com.example.rickyandmorty.data.api.exception.NoDataException
import com.example.rickyandmorty.databinding.FragmentLocationFilterBinding
import com.example.rickyandmorty.databinding.FragmentLocationsListBinding
import com.example.rickyandmorty.domain.model.locations.Locations
import com.example.rickyandmorty.presentation.adapters.location.list.LocationsPagingAdapter
import com.example.rickyandmorty.presentation.fragments.locations.detail.DetailLocationFragment
import com.example.rickyandmorty.presentation.fragments.locations.detail.DetailLocationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListLocationsFragment : Fragment(), LocationsPagingAdapter.LocationListener {
    private lateinit var binding: FragmentLocationsListBinding
    private lateinit var bindingFilter: FragmentLocationFilterBinding
    private var adapter = LocationsPagingAdapter(this)
    private lateinit var listLocationsViewModel: ListLocationsViewModel
    private val detailLocationViewModel: DetailLocationViewModel by activityViewModels()
    private var name = ""
    private var type = ""
    private var dimension = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLocationsListBinding.inflate(inflater, container, false)
        bindingFilter = FragmentLocationFilterBinding.inflate(inflater, container, false)
        listLocationsViewModel = ViewModelProvider(this)[ListLocationsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAllLocations(name)
        initProgressBar()
        findByName()
        initLocationsFilter()
        showBotNav()

    }

    private fun showBotNav() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun loadAllLocations(name: String) {
        binding.rvListLocations.adapter = adapter
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
                loadAllLocations(name)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val name = newText.toString()
                loadAllLocations(name)
                return true
            }
        })
    }

    private fun clearVariables() {
        name = ""
        type = ""
        dimension = ""
    }

    private fun initLocationsFilter() {
        binding.btnLocationsFilter.setOnClickListener {
            openLocationsFilter()
        }
    }

    private fun openLocationsFilter() = with(bindingFilter) {
        val dialog = BottomSheetDialog(requireContext())
        if (bindingFilter.root.parent != null) {
            (bindingFilter.root.parent as ViewGroup).removeView(bindingFilter.root)
        }
        dialog.setContentView(bindingFilter.root)
        dialog.show()
        clearVariables()
        btnCloseDialog.setOnClickListener { dialog.dismiss() }
        btnApply.setOnClickListener {
            checkFilterParams()
            if (name.isNotEmpty() || type.isNotEmpty() || dimension.isNotEmpty()) {
                loadAllLocations(name)
                dialog.dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_parameters),
                    Toast.LENGTH_SHORT
                ).show()
                clearVariables()
                loadAllLocations(name)
                dialog.dismiss()
            }
        }
    }

    private fun checkFilterParams() = with(bindingFilter) {
        if (chipPlanet.isChecked) type = "Planet"
        if (chipCluster.isChecked) type = "Cluster"
        if (chipSpaceStation.isChecked) type = "Space station"
        if (chipTv.isChecked) type = "TV"
        if (chipUnknownType.isChecked) type = "unknown"
        if (chipMicroverse.isChecked) type = "Microverse"
        if (chipResort.isChecked) type = "Resort"
        if (chipFantasyTown.isChecked) type = "Fantasy town"
        if (chipDream.isChecked) type = "Dream"
        if (chipMenagerie.isChecked) type = "Menagerie"
        if (chipGame.isChecked) type = "Game"
        if (chipCustoms.isChecked) type = "Customs"
        if (chipDaycare.isChecked) type = "Daycare"
        if (chip137.isChecked) dimension = "Dimension C-137"
        if (chipApocaliptic.isChecked) dimension = "Post-Apocalyptic Dimension"
        if (chipReplacement.isChecked) dimension = "Replacement Dimension"
        if (chipFantasy.isChecked) dimension = "Fantasy Dimension"
        if (chip5.isChecked) dimension = "Dimension 5-126"
        if (chipUnknownDimension.isChecked) dimension = "unknown"
    }

    override fun onClick(location: Locations) {
        detailLocationViewModel.onClickItemCharacter(location)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(
                R.id.containerFragment,
                DetailLocationFragment(detailLocationViewModel), "detailLocation"
            )
            .addToBackStack("listLocation")
            .commit()
    }
}
