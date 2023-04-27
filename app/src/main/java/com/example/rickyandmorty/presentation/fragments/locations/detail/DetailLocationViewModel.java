package com.example.rickyandmorty.presentation.fragments.locations.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rickyandmorty.data.api.NetworkApi;
import com.example.rickyandmorty.data.response.location.LocationResponse;
import com.example.rickyandmorty.domain.model.characters.Characters;
import com.example.rickyandmorty.domain.model.locations.Locations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailLocationViewModel extends ViewModel {
    public MutableLiveData<Locations> selectedItemLocation = new MutableLiveData<>();
    public MutableLiveData<List<Characters>> responseCharacters = new MutableLiveData<>();

    public MutableLiveData<String> locationName = new MutableLiveData<>();
    public List<String> listOfCharacters = new ArrayList<>();
    public String charactersIds;

    private final NetworkApi networkApi = NetworkApi.Companion.getInstance();

    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public void onClickItemCharacter(Locations location) {
        selectedItemLocation.setValue(location);
        setListOfCharacters(location);
    }

    public void setListOfCharacters(Locations location) {
        listOfCharacters
                .addAll(location
                        .getResidents());
    }

    public void setLocationName(String name){
        locationName.setValue(name);
        fetchDataLocation();
    }

    public void setResponseCharacter(List<Characters> post) {
        responseCharacters.setValue(post);

    }

    public void setResponseLocation(LocationResponse post) {
        selectedItemLocation.setValue(post.getResults().get(0));
        setListOfCharacters(post.getResults().get(0));
    }

    public MutableLiveData<Locations> getSelectedItemCharacter() {
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
