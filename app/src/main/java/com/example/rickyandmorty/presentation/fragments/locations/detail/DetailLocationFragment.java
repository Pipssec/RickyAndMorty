package com.example.rickyandmorty.presentation.fragments.locations.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.example.rickyandmorty.app.App;
import com.example.rickyandmorty.databinding.FragmentLocationDetailBinding;
import com.example.rickyandmorty.di.AppComponent;
import com.example.rickyandmorty.di.ViewModelFactory;
import com.example.rickyandmorty.domain.models.character.CharacterResult;
import com.example.rickyandmorty.domain.models.locations.Location;
import com.example.rickyandmorty.domain.models.locations.LocationResult;
import com.example.rickyandmorty.presentation.adapters.location.detail.DetailLocationCharacterAdapter;
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterFragment;
import com.example.rickyandmorty.presentation.fragments.characters.detail.DetailCharacterViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


public class DetailLocationFragment extends Fragment implements DetailLocationCharacterAdapter.SelectListener {
    private FragmentLocationDetailBinding binding;
    private DetailLocationViewModel detailLocationViewModel;

    private DetailCharacterViewModel detailCharacterViewModel;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RecyclerView recyclerCharactersIntoLocation;

    @Inject
    ViewModelFactory viewModelFactory;

    AppComponent component;


    @Override
    public void onAttach(@NonNull Context context) {
        App application = (App) requireActivity().getApplication();
        component = application.getAppComponent();
        component.injectDetailLocationFragment(this);
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLocationDetailBinding.inflate(inflater, container, false);
        detailCharacterViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailCharacterViewModel.class);
        detailLocationViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(DetailLocationViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerCharactersIntoLocation = binding.rvDetailLocationCharacters;
        recyclerCharactersIntoLocation.setHasFixedSize(true);
        hideBotNav();
        final Observer<LocationResult> observer = location -> {
            assert location != null;
            binding.tvNameLocationDetail.setText(location.getName());
            binding.tvDimensionDetail.setText(location.getDimension());
            binding.tvTypeLocationDetail.setText(location.getType());
        };
        detailLocationViewModel.getSelectedItemCharacter().observe(getViewLifecycleOwner(), observer);
        displayData();
        detailLocationViewModel.clearListOfCharacters();
        binding.btnBack.setOnClickListener(
                v -> requireActivity().getSupportFragmentManager().popBackStack()

        );

    }


    private void displayData() {
        detailLocationViewModel.getCharacters();
        detailLocationViewModel.fetchData();
        @SuppressLint("NotifyDataSetChanged") final Observer<List<CharacterResult>> observer = listOfCharacters -> {
            assert listOfCharacters != null;
            Log.d("listOfCharacters", listOfCharacters.toString());
            DetailLocationCharacterAdapter adapter = new DetailLocationCharacterAdapter(requireContext(), listOfCharacters, this);
            recyclerCharactersIntoLocation.setAdapter(adapter);
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
    public void onItemClicked(CharacterResult character) {
        detailCharacterViewModel.onClickItemCharacter(character);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.containerFragment, new DetailCharacterFragment())
                .addToBackStack(null)
                .commit();
    }
}
