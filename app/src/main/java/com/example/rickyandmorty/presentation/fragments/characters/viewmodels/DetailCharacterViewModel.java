package com.example.rickyandmorty.presentation.fragments.characters.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.rickyandmorty.domain.model.characters.Characters;

public class DetailCharacterViewModel extends ViewModel {
     private MutableLiveData<Characters> selectedItemCharacter = new MutableLiveData<>();

    public void onClickItemCharacter(Characters character) {
        selectedItemCharacter.setValue(character);
    }
}
