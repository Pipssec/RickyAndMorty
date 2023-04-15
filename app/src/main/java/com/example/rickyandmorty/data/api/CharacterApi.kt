package com.example.rickyandmorty.data.api


import com.example.rickyandmorty.data.response.CharactersResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CharacterApi {
    @GET("character/")
    suspend fun getAllCharacters(
        @Query("page") page: Int,
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("gender") gender: String
    ): Response<CharactersResponse>


    companion object {

        var characterApi: CharacterApi? = null
        fun getInstance(): CharacterApi {
            if (characterApi == null) {
                val baseurl = "https://rickandmortyapi.com/api/"
                val loginInterceptor = HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(loginInterceptor)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(baseurl)
                    .client(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()
                characterApi = retrofit.create(CharacterApi::class.java)
            }
            return characterApi!!
        }
    }
}