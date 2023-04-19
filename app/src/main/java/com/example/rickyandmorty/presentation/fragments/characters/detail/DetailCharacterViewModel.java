package com.example.rickyandmorty.presentation.fragments.characters.detail;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rickyandmorty.domain.model.characters.Characters;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailCharacterViewModel extends ViewModel {
     public MutableLiveData<Characters> selectedItemCharacter = new MutableLiveData<>();
     public List<String> listOfEpisodes = new ArrayList<>();
     public String episodesIds = getEpisodes();


    public void onClickItemCharacter(Characters character) {
        selectedItemCharacter.setValue(character);
        listOfEpisodes
                .addAll(character
                        .getEpisode());

    }

    public MutableLiveData<Characters> getSelectedItemCharacter() {
        return selectedItemCharacter;
    }

    public String getEpisodes(){
        String str1;
        String str2;
        String result = "";
        if(!listOfEpisodes.isEmpty()){
            for(String episode : listOfEpisodes){
                str1 = episode.substring(41);
                str2 = str1.replace('\"',',');
                result = result+str2;
                Log.d("EpisodesResult", result);
            }
        }
        return result;
    }
}
