package com.example.rickyandmorty.presentation.fragments.characters.list

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
import androidx.paging.PagingData
import com.example.rickyandmorty.R
import com.example.rickyandmorty.data.api.exception.BackendException
import com.example.rickyandmorty.data.api.exception.NoDataException
import com.example.rickyandmorty.databinding.FragmentCharacterFilterBinding
import com.example.rickyandmorty.databinding.FragmentCharactersListBinding
import com.example.rickyandmorty.domain.model.characters.Characters
import com.example.rickyandmorty.presentation.adapters.character.list.CharactersPagingAdapter
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterFragment
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListCharactersFragment : Fragment(), CharactersPagingAdapter.CharacterListener {
    private lateinit var binding: FragmentCharactersListBinding
    private lateinit var bindingFilter: FragmentCharacterFilterBinding
    lateinit var viewModelList: ListCharactersViewModel
    private val viewModelDetail: DetailCharacterViewModel by activityViewModels()
    private var adapter = CharactersPagingAdapter(this)
    private var name: String = ""
    private var status: String = ""
    private var gender: String = ""
    private var species: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingFilter = FragmentCharacterFilterBinding.inflate(inflater)
        binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        viewModelList = ViewModelProvider(this)[ListCharactersViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAllCharacters(name)
        showBotNav()
        findByName()
        initProgressBar()
        initCharactersFilter()
        swipeRefresh()
    }

    private fun findByName() {
        binding.searchInListCharacters.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val name = query.toString()
                loadAllCharacters(name)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val name = newText.toString()
                loadAllCharacters(name)
                return true
            }
        })
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

    private fun loadAllCharacters(name: String) {
        binding.rvListCharacters.adapter = adapter
        viewModelList.errorMessage.observe(requireActivity()) {
            Toast.makeText(requireContext(), "Error1", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            viewModelList.loadCharacters(name, status, gender, species)
            viewModelList.characterFlow.collectLatest(adapter::submitData)
        }
    }

    private fun clearVariables() {
        name = ""
        gender = ""
        species = ""
        status = ""
    }

    private fun initCharactersFilter() {
        binding.btnCharactersFilter.setOnClickListener {
            openCharacterFilter()
        }
    }

    private fun showBotNav() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }

    private fun openCharacterFilter() = with(bindingFilter) {
        val dialog = BottomSheetDialog(requireContext())
        if (bindingFilter.root.parent != null) {
            (bindingFilter.root.parent as ViewGroup).removeView(bindingFilter.root)
        }
        dialog.setContentView(bindingFilter.root)
        dialog.show()
        clearVariables()
        btnCloseDialog.setOnClickListener {
            dialog.dismiss()
        }
        btnApply.setOnClickListener {
            checkFilterParams()
            if (gender.isNotEmpty() || species.isNotEmpty() || status.isNotEmpty()) {
                loadAllCharacters(name)
                dialog.dismiss()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_parameters),
                    Toast.LENGTH_SHORT
                ).show()
                clearVariables()
                loadAllCharacters(name)
                dialog.dismiss()
            }
        }
    }

    private fun checkFilterParams() = with(bindingFilter) {
        if (chipAlive.isChecked) status = "Alive"
        if (chipDead.isChecked) status = "Dead"
        if (chipUnknown.isChecked) status = "unknown"
        if (chipFemale.isChecked) gender = "Female"
        if (chipMale.isChecked) gender = "Male"
        if (chipGenderless.isChecked) gender = "Genderless"
        if (chipUnknownGender.isChecked) gender = "unknown"
        if (chipHuman.isChecked) species = "Human"
        if (chipAlien.isChecked) species = "Alien"
        if (chipHumanoid.isChecked) species = "Humanoid"
        if (chipRobot.isChecked) species = "Robot"
        if (chipUnknownSpecies.isChecked) species = "unknown"
        if (chipPoopybutthole.isChecked) species = "Poopybutthole"
        if (chipMythological.isChecked) species = "Mythological"
        if (chipAnimal.isChecked) species = "Animal"
        if (chipCronenberg.isChecked) species = "Cronenberg"
        if (chipDisease.isChecked) species = "Disease"
    }

    override fun onClick(character: Characters) {
        viewModelDetail.onClickItemCharacter(character)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(
                R.id.containerFragment,
                DetailCharacterFragment(
                    viewModelDetail
                )
            )
            .addToBackStack("characters")
            .commit()
    }

    private fun swipeRefresh(){
        binding.swipeRefreshCharacters.setOnRefreshListener {
            lifecycleScope.launch{
                adapter.submitData(PagingData.empty())
                viewModelList.characterFlow.collectLatest(adapter::submitData)
            }
            binding.swipeRefreshCharacters.isRefreshing = false
        }
    }

}