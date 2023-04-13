package com.example.rickyandmorty.presentation.fragments.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.rickyandmorty.MyViewModelFactory
import com.example.rickyandmorty.R
import com.example.rickyandmorty.data.api.CharacterApi
import com.example.rickyandmorty.data.datasource.CharacterDataSource
import com.example.rickyandmorty.data.repository.CharactersRepository
import com.example.rickyandmorty.databinding.FragmentCharactersListBinding
import com.example.rickyandmorty.domain.model.Characters
import com.example.rickyandmorty.presentation.AppViewModel
import com.example.rickyandmorty.presentation.adapters.CharactersPagingAdapter
import kotlinx.coroutines.launch

class ListCharactersFragment: Fragment() {
    private lateinit var binding: FragmentCharactersListBinding
    lateinit var viewModel: AppViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofitService = CharacterApi.getInstance()
        val mainRepository = CharactersRepository(retrofitService)
        viewModel = ViewModelProvider(
            this,
            MyViewModelFactory(mainRepository)
        )[AppViewModel::class.java]
        val adapter = CharactersPagingAdapter(){ delegate ->
            viewModel.onClickItemCharacter(delegate)
            activity?.supportFragmentManager?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.replace(R.id.containerFragment, DetailCharacterFragment())
                ?.commit()
        }
        binding.rvInListCharacters.adapter = adapter

        viewModel.errorMessage.observe(requireActivity()) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

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
                    Toast.makeText(requireContext(), it.error.toString(), Toast.LENGTH_LONG).show()
                }

            }
        }

        lifecycleScope.launch {
            viewModel.getMovieList().observe(requireActivity()) {
                it?.let {
                    adapter.submitData(lifecycle, it)
                }
            }
        }
    }

}