package com.example.rickyandmorty.presentation.adapters.character.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rickyandmorty.R;
import com.example.rickyandmorty.domain.model.episodes.Episodes;

import java.util.List;



public class DetailCharacterEpisodesAdapter extends RecyclerView.Adapter<EpisodesViewHolder> {
    Context context;
    List<Episodes> postsList;

    public DetailCharacterEpisodesAdapter(Context context, List<Episodes> postsList) {
        this.context = context;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public EpisodesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_list_episodes,parent,false);
        return new EpisodesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesViewHolder holder, int position) {
        holder.tvNameEpisode.setText((postsList.get(position).getName()));
        holder.tvNumberEpisode.setText((postsList.get(position).getEpisode()));
        holder.tvAirDateEpisode.setText(postsList.get(position).getAir_date());

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }
}
