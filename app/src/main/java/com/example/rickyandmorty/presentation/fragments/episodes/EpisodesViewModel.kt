package com.example.rickyandmorty.presentation.fragments.episodes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.models.episodes.EpisodeResult
import com.example.rickyandmorty.domain.usecases.episode.EpisodeUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class EpisodesViewModel@Inject constructor(
    private val episodeUseCase: EpisodeUseCase
): ViewModel() {

    val selectedItemLocation = MutableLiveData<EpisodeResult>()
    val responseCharacterResult = MutableLiveData<List<CharacterResult?>?>()
    private val listOfCharacters = mutableListOf<List<String>>()
    private var charactersIds: String? = null
    private val networkApi = NetworkApi.getInstance()
    private val compositeDisposable = CompositeDisposable()


    val errorMessage = MutableLiveData<String>()

    var episodeFlow: Flow<PagingData<EpisodeResult>> = emptyFlow()

    fun onClickItemEpisodes(episode: EpisodeResult) {
        selectedItemLocation.value = episode
        listOfCharacters.add(episode.characters)
    }

    fun clearListCharacters() {
        listOfCharacters.clear()
    }

     fun fetchData() {
        compositeDisposable.add(networkApi.getDetailCharacter(charactersIds!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { post: List<CharacterResult?>? ->
                    this.responseCharacterResult.value = post
                }
            ) { throwable: Throwable? -> })
    }

    fun loadAllEpisodes(name: String, episode: String) {
        episodeFlow = Pager(PagingConfig(pageSize = 1)) {
            episodeUseCase.getEpisodes(name, episode)
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