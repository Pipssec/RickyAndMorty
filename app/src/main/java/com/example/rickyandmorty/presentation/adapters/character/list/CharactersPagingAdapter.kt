package com.example.rickyandmorty.presentation.adapters.character.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rickyandmorty.databinding.AdapterListCharactersBinding
import com.example.rickyandmorty.domain.models.character.CharacterResult

class CharactersPagingAdapter(private val listener: CharacterListener) :
    PagingDataAdapter<CharacterResult, CharactersPagingAdapter.CharactersViewHolder>(CharactersComporator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterListCharactersBinding.inflate(inflater, parent, false)
        return CharactersViewHolder(binding)
    }

    class CharactersViewHolder(val binding: AdapterListCharactersBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.binding.tvNameCharacter.text = item.name
        holder.binding.tvGenderCharacter.text = item.gender
        holder.binding.tvStatusCharacter.text = item.status
        holder.binding.tvSpeciesCharacter.text = item.species
        Glide.with(holder.binding.imIconCharacter)
            .load(item.image)
            .into(holder.binding.imIconCharacter)
        holder.itemView.rootView.setOnClickListener {
            listener.onClick(item)
        }



    }

    object CharactersComporator : DiffUtil.ItemCallback<CharacterResult>() {
        override fun areItemsTheSame(oldItem: CharacterResult, newItem: CharacterResult): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CharacterResult, newItem: CharacterResult): Boolean {
            return oldItem == newItem
        }
    }
    interface CharacterListener{
        fun onClick(character: CharacterResult)
    }
}