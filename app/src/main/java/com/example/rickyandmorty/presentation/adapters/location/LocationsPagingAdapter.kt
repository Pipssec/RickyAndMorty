package com.example.rickyandmorty.presentation.adapters.location

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.rickyandmorty.databinding.AdapterListLocationsBinding
import com.example.rickyandmorty.domain.model.locations.Locations

class LocationsPagingAdapter(private val listener: Listener) :
    PagingDataAdapter<Locations, LocationsPagingAdapter.LocationsViewHolder>(LocationsComporator) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterListLocationsBinding.inflate(inflater, parent, false)
        return LocationsViewHolder(binding)
    }

    class LocationsViewHolder(val binding: AdapterListLocationsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: LocationsViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.binding.tvNameLocation.text = item.name
        holder.binding.tvTypeLocation.text = item.type
        holder.binding.tvDimensionLocation.text = item.dimension
        holder.itemView.rootView.setOnClickListener {
            listener.onClick(item)
        }



    }

    object LocationsComporator : DiffUtil.ItemCallback<Locations>() {
        override fun areItemsTheSame(oldItem: Locations, newItem: Locations): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Locations, newItem: Locations): Boolean {
            return oldItem == newItem
        }
    }
    interface Listener{
        fun onClick(location: Locations)
    }
}