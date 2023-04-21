package com.example.rickyandmorty.presentation.fragments.locations.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickyandmorty.data.datasource.LocationDataSource
import com.example.rickyandmorty.domain.model.locations.Locations
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn

class ListLocationsViewModel: ViewModel() {
    val errorMessage = MutableLiveData<String>()

    var locationFlow: Flow<PagingData<Locations>> = emptyFlow()

    fun loadLocations(name: String, type: String, dimension: String) {
        locationFlow = Pager(PagingConfig(pageSize = 1)) {
            LocationDataSource(name, type, dimension)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }
}