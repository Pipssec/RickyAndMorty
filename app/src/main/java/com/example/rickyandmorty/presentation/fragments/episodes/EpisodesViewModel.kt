package com.example.rickyandmorty.presentation.fragments.episodes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.data.datasource.EpisodesDataSource
import com.example.rickyandmorty.domain.model.characters.Characters
import com.example.rickyandmorty.domain.model.episodes.Episodes
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn

class EpisodesViewModel: ViewModel() {

    val selectedItemLocation = MutableLiveData<Episodes>()
    val responseCharacters = MutableLiveData<List<Characters?>?>()
    private val listOfCharacters = mutableListOf<List<String>>()
    private var charactersIds: String? = null
    private val networkApi = NetworkApi.getInstance()
    private val compositeDisposable = CompositeDisposable()


    val errorMessage = MutableLiveData<String>()

    var episodesFlow: Flow<PagingData<Episodes>> = emptyFlow()

    fun onClickItemEpisodes(episode: Episodes) {
        selectedItemLocation.value = episode
        listOfCharacters.add(episode.characters)
    }

     fun fetchData() {
        compositeDisposable.add(networkApi.getDetailCharacter(charactersIds!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { post: List<Characters?>? ->
                    this.responseCharacters.value = post
                }
            ) { throwable: Throwable? -> })
    }

    fun loadAllEpisodes(name: String, episode: String) {
        episodesFlow = Pager(PagingConfig(pageSize = 1)) {
            EpisodesDataSource(name = name,episode = episode)
        }.flow.cachedIn(viewModelScope)
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
    }

     fun getCharacters() {
        var str1: String
        var result = ""
        if (listOfCharacters.isNotEmpty()) {
            for (episode in listOfCharacters[0]) {
                str1 = episode.substring(42)
                result = "$result+$str1,"
            }
        }
        charactersIds = result
    }
}