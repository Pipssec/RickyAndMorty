package com.example.rickyandmorty.presentation.fragments.locations.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rickyandmorty.data.api.NetworkApi;
import com.example.rickyandmorty.data.api.response.location.LocationResponse;
import com.example.rickyandmorty.domain.models.character.CharacterResult;
import com.example.rickyandmorty.domain.models.locations.Location;
import com.example.rickyandmorty.domain.models.locations.LocationResult;

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

    private final NetworkApi networkApi = NetworkApi.Companion.getInstance();

    @Inject
    public DetailLocationViewModel(){}

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onClickItemCharacter(LocationResult location) {
        selectedItemLocation.setValue(location);
        setListOfCharacters(location);
    }

    public void setListOfCharacters(LocationResult location) {
        listOfCharacters
                .addAll(location
                        .getResidents());
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
        setListOfCharacters(post.getResults().get(0));
    }

    public MutableLiveData<LocationResult> getSelectedItemCharacter() {
        return selectedItemLocation;
    }

    public void clearListOfCharacters() {
        listOfCharacters.clear();
    }

    void fetchData() {
        compositeDisposable.add(networkApi.getDetailCharacter(charactersIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setResponseCharacter, throwable -> {
                }));
    }

    void fetchDataLocation() {
        compositeDisposable.add(networkApi.getDetailLocation(locationName.getValue())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setResponseLocation, throwable -> {
                }));
    }

    private void setResponseLocation(LocationResponse locationResponse) {
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
