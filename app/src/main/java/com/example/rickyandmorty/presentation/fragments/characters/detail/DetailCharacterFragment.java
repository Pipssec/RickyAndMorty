package com.example.rickyandmorty.presentation.fragments.characters.detail;


import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rickyandmorty.R;
import com.example.rickyandmorty.app.App;
import com.example.rickyandmorty.databinding.FragmentCharacterDetailBinding;
import com.example.rickyandmorty.di.AppComponent;
import com.example.rickyandmorty.di.ViewModelFactory;
import com.example.rickyandmorty.domain.models.character.CharacterResult;
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult;
import com.example.rickyandmorty.presentation.adapters.character.detail.DetailCharacterEpisodesAdapter;
import com.example.rickyandmorty.presentation.fragments.episodes.EpisodesViewModel;
import com.example.rickyandmorty.presentation.fragments.episodes.detail.DetailEpisodesFragment;
import com.example.rickyandmorty.presentation.fragments.locations.detail.DetailLocationFragment;
import com.example.rickyandmorty.presentation.fragments.locations.detail.DetailLocationViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class DetailCharacterFragment extends Fragment implements DetailCharacterEpisodesAdapter.EpisodeListener {
    private FragmentCharacterDetailBinding binding;
    @Inject
    ViewModelFactory viewModelFactory;
    private DetailCharacterViewModel detailCharacterViewModel;
    private EpisodesViewModel episodeViewModel;
    private DetailLocationViewModel detailLocationViewModel;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recyclerEpisodesIntoCharacter;
    AppComponent component;


    @Override
    public void onAttach(@NonNull Context context) {
        App application = (App) requireActivity().getApplication();
        component = application.getAppComponent();
        component.injectDetailCharacterFragment(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCharacterDetailBinding.inflate(inflater, container, false);
        episodeViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(EpisodesViewModel.class);
        detailLocationViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailLocationViewModel.class);
        detailCharacterViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailCharacterViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerEpisodesIntoCharacter = binding.recyclerDetailCharacterEpisodes;
        recyclerEpisodesIntoCharacter.setHasFixedSize(true);
        hideBotNav();
        ShapeableImageView ivIconCharacter = binding.ivIconDetailCharacter;
        final Observer<CharacterResult> observer = character1 -> {
            assert character1 != null;
            Glide.with(requireContext())
                    .load(character1.getImage())
                    .into(ivIconCharacter);
            binding.tvNameDetailCharacter.setText(character1.getName());
            binding.tvSpeciesDetailCharacter.setText(character1.getSpecies());
            binding.tvGenderDetailCharacter.setText(character1.getGender());
            binding.tvOriginDetailCharacter.setText(character1.getOrigin().getName());
            binding.tvLocationDetailCharacter.setText(character1.getLocation().getName());
            binding.tvOriginDetailCharacter.setOnClickListener(v -> {
                if (isNetworkConnected()) {
                    detailLocationViewModel.setLocationName(character1.getOrigin().getName());
                } else {
                    detailLocationViewModel.setLocationNameIsNotConn(character1.getOrigin().getName());
                }
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.containerFragment, new DetailLocationFragment())
                        .addToBackStack(null)
                        .commit();
            });
            binding.tvLocationDetailCharacter.setOnClickListener(v -> {
                if (isNetworkConnected()) {
                    detailLocationViewModel.setLocationName(character1.getLocation().getName());
                } else {
                    detailLocationViewModel.setLocationNameIsNotConn(character1.getLocation().getName());
                }
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.containerFragment, new DetailLocationFragment())
                        .addToBackStack(null)
                        .commit();
            });
        };

        detailCharacterViewModel.getSelectedItemCharacter().observe(getViewLifecycleOwner(), observer);
        detailCharacterViewModel.getEpisodes();
        detailCharacterViewModel.fetchData();
        displayData();
        detailCharacterViewModel.clearListOfEpisodes();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }


    private void displayData() {
        final Observer<List<EpisodeResult>> observer = listOfEpisodes -> {
            DetailCharacterEpisodesAdapter adapter = new DetailCharacterEpisodesAdapter(requireContext(), listOfEpisodes, this);
            recyclerEpisodesIntoCharacter.setAdapter(adapter);
        };
        detailCharacterViewModel.responseEpisodes.observe(getViewLifecycleOwner(), observer);
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

    public void hideBotNav() {
        BottomNavigationView botNav = requireActivity().findViewById(R.id.bottomNavigationView);
        botNav.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(EpisodeResult episode) {
        episodeViewModel.onClickItemEpisodes(episode);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.containerFragment, new DetailEpisodesFragment())
                .addToBackStack(null)
                .commit();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
