package com.example.rickyandmorty.presentation.fragments.characters.detail;

import android.net.Network;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rickyandmorty.data.api.NetworkApi;
import com.example.rickyandmorty.data.response.episodes.EpisodesResponse;
import com.example.rickyandmorty.domain.model.characters.Characters;
import com.example.rickyandmorty.domain.model.episodes.Episodes;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailCharacterViewModel extends ViewModel {
    public MutableLiveData<Characters> selectedItemCharacter = new MutableLiveData<>();
    public MutableLiveData<List<Episodes>> responseEpisodes = new MutableLiveData<>();
    public List<String> listOfEpisodes = new ArrayList<>();
    public String episodesIds;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    public NetworkApi networkApi = NetworkApi.Companion.getInstance();

    @Inject
    public DetailCharacterViewModel(){}


    public void onClickItemCharacter(Characters character) {
        selectedItemCharacter.setValue(character);
        listOfEpisodes
                .addAll(character
                        .getEpisode());

    }

    public void setListOfEpisodes(List<Episodes> post){
        responseEpisodes.setValue(post);
    }

    public MutableLiveData<Characters> getSelectedItemCharacter() {
        return selectedItemCharacter;
    }

    public void clearListOfEpisodes() {
        listOfEpisodes.clear();
    }

    void fetchData() {
        compositeDisposable.add(networkApi.getDetailEpisode(episodesIds)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setListOfEpisodes, throwable -> {
                }));
    }

    public void getEpisodes() {
        String str1;
        String result = "";
        if (!listOfEpisodes.isEmpty()) {
            for (String episode : listOfEpisodes) {
                str1 = episode.substring(40);
                result = result + str1 + ",";
            }
        }
        episodesIds = result;
    }


}
