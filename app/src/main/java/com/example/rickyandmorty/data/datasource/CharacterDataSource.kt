package com.example.rickyandmorty.data.datasource

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.rickyandmorty.data.api.ext.backendException
import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.repository.CharacterRepository
import javax.inject.Inject

class CharacterDataSource@Inject constructor(
    private val repository: CharacterRepository,
    private val application: Application,
    private val name: String,
    private val status: String,
    private val gender: String,
    private val species: String,
) : PagingSource<Int, CharacterResult>() {
    companion object {
        private const val START_PAGE = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterResult> {
        try {
            var nextKey: Int? = 1
            val page = params.key ?: START_PAGE
            val responseData = arrayListOf<CharacterResult>()
            if(hasConnected(application.applicationContext)) {
                val response = repository.getCharacter(page,name,gender,status,species)
                responseData.addAll(response.result)
                nextKey = if(response.info.next == null && response.result.isEmpty()) null else page+1
            } else {
                val listCharacters = repository.getListCharacters()
                responseData.addAll(listCharacters)
                Log.d("CharacterRes", responseData.size.toString())
                nextKey = if(responseData.isEmpty()) page+1 else null
                Log.d("CharacterKey", nextKey.toString())
            }

            val prevKey = if (page == START_PAGE) null else page - 1
            return LoadResult.Page(
                data = responseData,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            return LoadResult.Error(backendException(e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterResult>): Int? = null

    private fun hasConnected(context: Context): Boolean{
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = manager.activeNetworkInfo
        return network != null && network.isConnected
    }
}