package com.example.rickyandmorty.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickyandmorty.databinding.AdapterListCharactersBinding
import com.example.rickyandmorty.domain.model.Characters

class CharactersPagingAdapter(private val delegate: (Characters) -> Unit) :
    PagingDataAdapter<Characters, CharactersPagingAdapter.CharactersViewHolder>(CharactersComporator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterListCharactersBinding.inflate(inflater, parent, false)
        return CharactersViewHolder(binding)
    }

    class CharactersViewHolder(val binding: AdapterListCharactersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.binding.tvName.text = item.name
        holder.binding.tvGender.text = item.gender
        holder.binding.tvStatus.text = item.status
        holder.binding.tvSpecies.text = item.species
        Glide.with(holder.binding.imIconCharacter)
            .load(item.image)
            .into(holder.binding.imIconCharacter)
        holder.itemView.setOnClickListener {
            delegate(item)
        }


    }

    object CharactersComporator : DiffUtil.ItemCallback<Characters>() {
        override fun areItemsTheSame(oldItem: Characters, newItem: Characters): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Characters, newItem: Characters): Boolean {
            return oldItem == newItem
        }
    }
}