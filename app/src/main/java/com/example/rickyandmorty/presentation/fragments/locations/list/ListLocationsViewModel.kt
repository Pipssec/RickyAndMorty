package com.example.rickyandmorty.presentation.fragments.locations.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickyandmorty.domain.models.locations.LocationResult
import com.example.rickyandmorty.domain.usecases.location.LocationUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class ListLocationsViewModel@Inject constructor(
    private val locationUseCase: LocationUseCase
): ViewModel() {
    val errorMessage = MutableLiveData<String>()

    var locationFlow: Flow<PagingData<LocationResult>> = emptyFlow()

    fun loadLocations(name: String, type: String, dimension: String) {
        locationFlow = Pager(PagingConfig(pageSize = 10, enablePlaceholders = false, initialLoadSize = 10)) {
            locationUseCase.getLocation(name, type, dimension)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }
}