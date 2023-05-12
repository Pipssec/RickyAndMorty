package com.example.rickyandmorty.domain.repository

import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.models.locations.Location
import com.example.rickyandmorty.domain.models.locations.LocationResult
import io.reactivex.Observable


interface LocationRepository {

    suspend fun getLocation(page: Int, name: String, type: String, dimension: String): Location

    suspend fun insertLocation(list: List<LocationResult>)

    suspend fun getListLocationsDb(
                            offset: Int,
                            limit: Int,
                            name: String,
                            type: String,
                            dimension: String
    ): List<LocationResult>

    fun getDetailCharacter(id: String): Observable<List<CharacterResult>>

    fun getDetailLocation(name: String): Observable<Location>

}