package com.example.rickyandmorty.presentation.adapters.character.detail;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickyandmorty.R;

public class EpisodesViewHolder extends RecyclerView.ViewHolder {
    TextView tvNameEpisode,tvNumberEpisode,tvAirDateEpisode;

    public EpisodesViewHolder(@NonNull View itemView) {
        super(itemView);

        tvNumberEpisode = itemView.findViewById(R.id.tvNumberEpisode);
        tvNameEpisode = itemView.findViewById(R.id.tvNameEpisode);
        tvAirDateEpisode = itemView.findViewById(R.id.tvAirDateEpisode);
    }
}
