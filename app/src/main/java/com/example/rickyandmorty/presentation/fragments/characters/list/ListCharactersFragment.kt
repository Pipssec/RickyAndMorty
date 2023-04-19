package com.example.rickyandmorty.presentation.fragments.characters.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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
import com.example.rickyandmorty.databinding.FragmentCharactersListBinding
import com.example.rickyandmorty.domain.model.characters.Characters
import com.example.rickyandmorty.presentation.adapters.CharactersPagingAdapter
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterFragment
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListCharactersFragment: Fragment(), CharactersPagingAdapter.Listener {
    private lateinit var binding: FragmentCharactersListBinding
    lateinit var viewModelList: ListCharactersViewModel
    private val viewModelDetail: DetailCharacterViewModel by activityViewModels()
    private var adapter = CharactersPagingAdapter(this)
    private var name: String = ""
    private var status: String = ""
    private var gender: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        viewModelList = ViewModelProvider(this)[ListCharactersViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBotNav()
        binding.rvListCharacters.adapter = adapter
        loadAllCharacters()
        findByName()
        initProgressBar()
        showCharactersFilter()
    }

    private fun findByName(){
        binding.rvListCharacters.adapter = adapter
        binding.searchInListCharacters.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                var name = query.toString()
                lifecycleScope.launch {
                    viewModelList.loadCharacters(name,status,gender)
                    viewModelList.characterFlow.collectLatest(adapter::submitData)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                var name = newText.toString()
                lifecycleScope.launch {
                    viewModelList.loadCharacters(name,status,gender)
                    viewModelList.characterFlow.collectLatest(adapter::submitData)
                }
                return true
            }

        })
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
                    Toast.makeText(requireContext(), "Данных не найдено", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadAllCharacters(){
        viewModelList.errorMessage.observe(requireActivity()) {
            Toast.makeText(requireContext(), "Error1", Toast.LENGTH_SHORT).show()
        }
        lifecycleScope.launch {
            viewModelList.loadCharacters(name, gender, status)
            viewModelList.characterFlow.collectLatest(adapter::submitData)
        }
    }

    private fun showCharactersFilter(){
        binding.btnCharactersFilter.setOnClickListener{
            openCharacterFilter()
        }
    }
    private fun showBotNav() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }
    private fun openCharacterFilter(){
        val dialogView: View = layoutInflater.inflate(R.layout.character_filter_fragment, null)
        val dialog = BottomSheetDialog(requireContext())
        val btnApply = dialogView.findViewById<Button>(R.id.btnApply)
        val chipAlive = dialogView.findViewById<Chip>(R.id.chip_alive)
        val chipDead = dialogView.findViewById<Chip>(R.id.chip_dead)
        val chipUnknown = dialogView.findViewById<Chip>(R.id.chip_unknown)
        val chipFemale = dialogView.findViewById<Chip>(R.id.chip_female)
        val chipMale = dialogView.findViewById<Chip>(R.id.chip_male)
        val chipGenderless = dialogView.findViewById<Chip>(R.id.chip_genderless)
        val chipUnknownGender = dialogView.findViewById<Chip>(R.id.chip_unknown_gender)
        val btnCloseDialog = dialogView.findViewById<ImageView>(R.id.btnCloseDialog)
        dialog.setContentView(dialogView)
        dialog.show()
        btnCloseDialog.setOnClickListener{ dialog.dismiss() }
        when(status){
            "Alive" -> chipAlive.isChecked
            "Dead" -> chipDead.isChecked
            "unknown" -> chipUnknown.isChecked
        }
        when(gender){
            "Female" -> chipFemale.isChecked
            "Male" -> chipMale.isChecked
            "Genderless" -> chipGenderless.isChecked
            "unknown" -> chipUnknownGender.isChecked
        }
        btnApply.setOnClickListener {
            if(chipAlive.isChecked) status = "Alive"
            if(chipDead.isChecked) status = "Dead"
            if(chipUnknown.isChecked) status = "unknown"
            if(chipFemale.isChecked) gender = "Female"
            if(chipMale.isChecked) gender = "Male"
            if(chipGenderless.isChecked) gender = "Genderless"
            if(chipUnknownGender.isChecked) gender = "unknown"
            if(chipAlive.isChecked || chipDead.isChecked || chipFemale.isChecked || chipGenderless.isChecked ||
                chipMale.isChecked || chipFemale.isChecked || chipUnknown.isChecked || chipUnknownGender.isChecked){
                lifecycleScope.launch {
                    viewModelList.loadCharacters(name,status,gender)
                    viewModelList.characterFlow.collectLatest(adapter::submitData)

                }
                dialog.dismiss()
                binding.btnCharactersFilter.visibility = View.GONE
            } else {
                Toast.makeText(requireContext(),getString(R.string.error_parameters),Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onClick(character: Characters) {
        viewModelDetail.onClickItemCharacter(character)
        val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
        fragmentManager
            .beginTransaction()
            .replace(R.id.containerFragment,
                DetailCharacterFragment(
                    viewModelDetail
                )
            )
            .addToBackStack("characters")
            .commit()
    }

}