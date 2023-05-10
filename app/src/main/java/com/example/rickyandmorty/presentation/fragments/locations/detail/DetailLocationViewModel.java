package com.example.rickyandmorty.presentation.fragments.locations.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.rickyandmorty.domain.models.character.CharacterResult;
import com.example.rickyandmorty.domain.models.locations.Location;
import com.example.rickyandmorty.domain.models.locations.LocationResult;
import com.example.rickyandmorty.domain.repository.LocationRepository;
import com.example.rickyandmorty.domain.usecases.location.LocationUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailLocationViewModel extends ViewModel {
    public MutableLiveData<LocationResult> selectedItemLocation = new MutableLiveData<>();
    public MutableLiveData<List<CharacterResult>> responseCharacters = new MutableLiveData<>();
    public MutableLiveData<String> locationName = new MutableLiveData<>();
    public List<String> listOfCharacters = new ArrayList<>();
    public String charactersIds;
    LocationUseCase locationUseCase;


    @Inject
    public DetailLocationViewModel(LocationUseCase locationUseCase){
        this.locationUseCase = locationUseCase;
    }

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onClickItemCharacter(LocationResult location) {
        selectedItemLocation.setValue(location);
        listOfCharacters.addAll(location.getResidents());
        getCharacters();
        fetchData();
    }


    public void setLocationName(String name){
        locationName.setValue(name);
        fetchDataLocation();

    }

    public void setResponseCharacter(List<CharacterResult> post) {
        responseCharacters.setValue(post);

    }

    public void setResponseLocation(Location post) {
        selectedItemLocation.setValue(post.getResults().get(0));
        onClickItemCharacter(post.getResults().get(0));
    }

    public MutableLiveData<LocationResult> getSelectedItemCharacter() {
        return selectedItemLocation;
    }

    public void clearListOfCharacters() {
        listOfCharacters.clear();
    }

    void fetchData() {
        compositeDisposable.add(locationUseCase.getDetailCharacter(charactersIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setResponseCharacter, throwable -> {
                }));
    }

    void fetchDataLocation() {
        compositeDisposable.add(locationUseCase.getDetailLocation(locationName.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setResponseLocation, throwable -> {
                }));
    }



    public void getCharacters() {
        String str1;
        String result = "";
        if (!listOfCharacters.isEmpty()) {
            for (String episode : listOfCharacters) {
                str1 = episode.substring(42);
                result = result + str1 + ",";
            }
        }
        charactersIds = result;
    }
}
