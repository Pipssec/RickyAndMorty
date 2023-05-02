package com.example.rickyandmorty.data.api


import com.example.rickyandmorty.data.response.characters.CharactersResponse
import com.example.rickyandmorty.data.response.episodes.EpisodesResponse
import com.example.rickyandmorty.data.response.location.LocationResponse
import com.example.rickyandmorty.domain.model.characters.Characters
import com.example.rickyandmorty.domain.model.episodes.Episodes
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NetworkApi {
    @GET("character/")
    suspend fun getAllCharacters(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("gender") gender: String,
        @Query("species") species: String
    ): CharactersResponse

    @GET("location/")
    suspend fun getAllLocation(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("type") type: String,
        @Query("dimension") dimension: String
    ): LocationResponse

    @GET("episode/")
    suspend fun getAllEpisode(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("episode") episode: String
    ): EpisodesResponse


    @GET("episode/{id}")
    fun getDetailEpisode(@Path("id") id: String): Observable<List<Episodes>>

    @GET("character/{id}")
    fun getDetailCharacter(@Path("id") id: String): Observable<List<Characters>>

    @GET("location/")
    fun getDetailLocation(@Query("name") name: String): Observable<LocationResponse>


    companion object {

        var networkApi: NetworkApi? = null
        fun getInstance(): NetworkApi {
            if (networkApi == null) {
                val baseurl = "https://rickandmortyapi.com/api/"
                val loginInterceptor = HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(loginInterceptor)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseurl)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                networkApi = retrofit.create(NetworkApi::class.java)
            }
            return networkApi!!
        }
    }
}