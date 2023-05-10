package com.example.rickyandmorty.presentation.adapters.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickyandmorty.databinding.AdapterListEpisodesBinding
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult

class EpisodesPagingAdapter(private val listener: EpisodeListener) :
    PagingDataAdapter<EpisodeResult, EpisodesPagingAdapter.EpisodesViewHolder>(EpisodesComporator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterListEpisodesBinding.inflate(inflater, parent, false)
        return EpisodesViewHolder(binding)
    }

    class EpisodesViewHolder(val binding: AdapterListEpisodesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: EpisodesViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.binding.tvNameEpisode.text = item.name
        holder.binding.tvNumberEpisode.text = item.episode
        holder.binding.tvAirDateEpisode.text = item.air_date
        holder.itemView.rootView.setOnClickListener {
            listener.onClick(item)
        }



    }

    object EpisodesComporator : DiffUtil.ItemCallback<EpisodeResult>() {
        override fun areItemsTheSame(oldItem: EpisodeResult, newItem: EpisodeResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: EpisodeResult, newItem: EpisodeResult): Boolean {
            return oldItem == newItem
        }
    }
    interface EpisodeListener{
        fun onClick(episode: EpisodeResult)
    }
}