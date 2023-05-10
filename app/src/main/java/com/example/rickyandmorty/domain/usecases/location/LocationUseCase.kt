package com.example.rickyandmorty.domain.usecases.location

import android.app.Application
import com.example.rickyandmorty.data.datasource.LocationDataSource
import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.models.locations.Location
import com.example.rickyandmorty.domain.repository.LocationRepository
import io.reactivex.Observable
import javax.inject.Inject

class LocationUseCase @Inject constructor(
    private val repository: LocationRepository,
    private val application: Application
) {
    fun getLocation(name: String, type: String, dimension: String): LocationDataSource {
        return LocationDataSource(repository, application, name, type, dimension)
    }
    fun getDetailCharacter(id: String): Observable<List<CharacterResult>>{
        return repository.getDetailCharacter(id)
    }

    fun getDetailLocation(name: String): Observable<Location>{
        return repository.getDetailLocation(name)
    }
}