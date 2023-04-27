package com.example.rickyandmorty.presentation.fragments.locations.detail;

import android.annotation.SuppressLint;
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

import com.example.rickyandmorty.R;
import com.example.rickyandmorty.data.api.NetworkApi;
import com.example.rickyandmorty.databinding.FragmentLocationDetailBinding;
import com.example.rickyandmorty.domain.model.characters.Characters;
import com.example.rickyandmorty.domain.model.locations.Locations;
import com.example.rickyandmorty.presentation.adapters.location.detail.DetailLocationCharacterAdapter;
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterFragment;
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;


public class DetailLocationFragment extends Fragment implements DetailLocationCharacterAdapter.SelectListener {
    private FragmentLocationDetailBinding binding;
    private DetailLocationViewModel detailLocationViewModel;

    private DetailCharacterViewModel detailCharacterViewModel;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recyclerCharactersIntoLocation;


    public DetailLocationFragment(@NotNull DetailLocationViewModel detailLocationViewModel) {
        this.detailLocationViewModel = detailLocationViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationDetailBinding.inflate(inflater, container, false);
        detailCharacterViewModel = new ViewModelProvider(this).get(DetailCharacterViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerCharactersIntoLocation = binding.rvDetailLocationCharacters;
        recyclerCharactersIntoLocation.setHasFixedSize(true);
        hideBotNav();
        final Observer<Locations> observer = location -> {
            assert location != null;
            binding.tvNameLocationDetail.setText(location.getName());
            binding.tvDimensionDetail.setText(location.getDimension());
            binding.tvTypeLocationDetail.setText(location.getType());
            detailLocationViewModel.setListOfCharacters(location);
        };
        detailLocationViewModel.getSelectedItemCharacter().observe(getViewLifecycleOwner(), observer);
        detailLocationViewModel.getCharacters();
        detailLocationViewModel.fetchData();
        displayData();
        detailLocationViewModel.clearListOfCharacters();
        binding.btnBack.setOnClickListener(v -> getActivity().getSupportFragmentManager().popBackStack());

    }


    private void displayData() {
        @SuppressLint("NotifyDataSetChanged") final Observer<List<Characters>> observer = listOfCharacters -> {
            assert listOfCharacters != null;
            DetailLocationCharacterAdapter adapter = new DetailLocationCharacterAdapter(requireContext(), listOfCharacters, this);
            recyclerCharactersIntoLocation.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        };
        detailLocationViewModel.responseCharacters.observe(getViewLifecycleOwner(), observer);

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
    public void onItemClicked(Characters character) {
        detailCharacterViewModel.onClickItemCharacter(character);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.containerFragment, new DetailCharacterFragment(detailCharacterViewModel))
                .addToBackStack(null)
                .commit();
    }
}
