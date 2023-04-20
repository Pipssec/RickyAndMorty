package com.example.rickyandmorty.presentation.fragments.characters.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rickyandmorty.R;
import com.example.rickyandmorty.data.api.NetworkApi;
import com.example.rickyandmorty.databinding.FragmentCharacterDetailBinding;
import com.example.rickyandmorty.domain.model.characters.Characters;
import com.example.rickyandmorty.domain.model.episodes.Episodes;
import com.example.rickyandmorty.presentation.adapters.character.detail.DetailCharacterEpisodesAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;

import org.jetbrains.annotations.NotNull;


import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DetailCharacterFragment extends Fragment {
        private FragmentCharacterDetailBinding binding;
        private DetailCharacterViewModel detailCharacterViewModel;
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        RecyclerView recyclerEpisodesIntoCharacter;
        NetworkApi networkApi;


        public DetailCharacterFragment(@NotNull DetailCharacterViewModel viewModelDetail) {
                this.detailCharacterViewModel = viewModelDetail;
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                binding = FragmentCharacterDetailBinding.inflate(inflater, container, false);
//                detailCharacterViewModel = new ViewModelProvider(this).get(DetailCharacterViewModel.class);
                return binding.getRoot();
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);
                networkApi = NetworkApi.Companion.getNetworkApi();
                recyclerEpisodesIntoCharacter = binding.recyclerDetailCharacterEpisodes;
                recyclerEpisodesIntoCharacter.setHasFixedSize(true);
                hideBotNav();
                ShapeableImageView ivIconCharacter = binding.ivIconDetailCharacter;
                TextView tvNameCharacter = binding.tvNameDetailCharacter;
                TextView tvSpeciesCharacter = binding.tvSpeciesDetailCharacter;
                TextView tvGenderCharacter = binding.tvGenderDetailCharacter;
                TextView tvOriginCharacter = binding.tvOriginDetailCharacter;
                TextView tvLocationCharacter = binding.tvLocationDetailCharacter;
                final Observer<Characters> observer = character1 -> {
                        assert character1 != null;
                        Glide.with(requireContext())
                                .load(character1.getImage())
                                .into(ivIconCharacter);
                        tvNameCharacter.setText(character1.getName());
                        tvSpeciesCharacter.setText(character1.getSpecies());
                        tvGenderCharacter.setText(character1.getGender());
                        tvOriginCharacter.setText(character1.getOrigin().getName());
                        tvLocationCharacter.setText(character1.getLocation().getName());

                };
                detailCharacterViewModel.getSelectedItemCharacter().observe(getViewLifecycleOwner(), observer);
                detailCharacterViewModel.getEpisodes();
                fetchData();
        }
        private void fetchData() {
                compositeDisposable.add(networkApi.getDetailEpisode(detailCharacterViewModel.episodesIds)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<List<Episodes>>() {
                                @Override
                                public void accept(List<Episodes> posts) throws Exception {
                                        displayData(posts);
                                }
                        }));
        }

        private void displayData(List<Episodes> posts) {
                DetailCharacterEpisodesAdapter adapter = new DetailCharacterEpisodesAdapter(requireContext(),posts);
                recyclerEpisodesIntoCharacter.setAdapter(adapter);
        }
        @Override
        public void onStop() {
                compositeDisposable.clear();
                super.onStop();
        }

        public void hideBotNav(){
                BottomNavigationView botNav = requireActivity().findViewById(R.id.bottomNavigationView);
                botNav.setVisibility(View.GONE);
        }
}
