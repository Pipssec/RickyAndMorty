package com.example.rickyandmorty.presentation.fragments.episodes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickyandmorty.data.datasource.EpisodesDataSource
import com.example.rickyandmorty.domain.model.episodes.Episodes
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn

class EpisodesViewModel: ViewModel() {
    val errorMessage = MutableLiveData<String>()

    var episodesFlow: Flow<PagingData<Episodes>> = emptyFlow()

    fun loadAllEpisodes(name: String, episode: String) {
        episodesFlow = Pager(PagingConfig(pageSize = 1)) {
            EpisodesDataSource(name = name,episode = episode)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }
}