package com.example.rickyandmorty.presentation.adapters.location.detail;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rickyandmorty.R;
import com.google.android.material.imageview.ShapeableImageView;


public class CharactersViewHolder extends RecyclerView.ViewHolder {

    TextView tvName, tvSpecies, tvStatus, tvGender;
    ShapeableImageView tvImage;
    public CharactersViewHolder(View view) {
        super(view);
        tvName = view.findViewById(R.id.tvNameCharacter);
        tvImage = view.findViewById(R.id.imIconCharacter);
        tvSpecies = view.findViewById(R.id.tvSpeciesCharacter);
        tvStatus = view.findViewById(R.id.tvStatusCharacter);
        tvGender = view.findViewById(R.id.tvGenderCharacter);
    }
}
