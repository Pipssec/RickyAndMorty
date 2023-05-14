package com.example.rickyandmorty.presentation.fragments.characters.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rickyandmorty.data.api.NetworkApi;
import com.example.rickyandmorty.domain.models.character.CharacterResult;
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult;
import com.example.rickyandmorty.domain.repository.CharacterRepository;
import com.example.rickyandmorty.domain.usecases.character.CharacterUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailCharacterViewModel extends ViewModel {
    public MutableLiveData<CharacterResult> selectedItemCharacter = new MutableLiveData<>();
    public MutableLiveData<List<EpisodeResult>> responseEpisodes = new MutableLiveData<>();
    public List<String> listOfEpisodes = new ArrayList<>();
    public String episodesIds;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    CharacterUseCase characterUseCase;

    @Inject
    public DetailCharacterViewModel(
            CharacterUseCase characterUseCase
    ) {
        this.characterUseCase = characterUseCase;
    }


    public void onClickItemCharacter(CharacterResult character) {
        selectedItemCharacter.setValue(character);
        listOfEpisodes
                .addAll(character
                        .getEpisode());

    }

    public void setListOfEpisodes(List<EpisodeResult> post) {
        responseEpisodes.setValue(post);
    }

    public MutableLiveData<CharacterResult> getSelectedItemCharacter() {
        return selectedItemCharacter;
    }

    public void clearListOfEpisodes() {
        listOfEpisodes.clear();
    }

    void fetchData() {
        compositeDisposable.add(characterUseCase.getDetailEpisode(episodesIds)
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
