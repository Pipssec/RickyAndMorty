package com.example.rickyandmorty.presentation.adapters.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickyandmorty.databinding.AdapterListEpisodesBinding
import com.example.rickyandmorty.domain.model.episodes.Episodes

class EpisodesPagingAdapter(private val listener: Listener) :
    PagingDataAdapter<Episodes, EpisodesPagingAdapter.EpisodesViewHolder>(EpisodesComporator) {
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

    object EpisodesComporator : DiffUtil.ItemCallback<Episodes>() {
        override fun areItemsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Episodes, newItem: Episodes): Boolean {
            return oldItem == newItem
        }
    }
    interface Listener{
        fun onClick(episode: Episodes)
    }
}