package com.example.rickyandmorty.data.repository

import com.example.rickyandmorty.data.api.NetworkApi
import com.example.rickyandmorty.data.db.dao.LocationDao
import com.example.rickyandmorty.data.mappers.LocationMapper
import com.example.rickyandmorty.domain.models.character.CharacterResult
import com.example.rickyandmorty.domain.models.locations.Location
import com.example.rickyandmorty.domain.models.locations.LocationResult
import com.example.rickyandmorty.domain.repository.LocationRepository
import io.reactivex.Observable
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val apiService: NetworkApi,
    private val locationDao: LocationDao,
    private val mapper: LocationMapper
): LocationRepository {

    override suspend fun getLocation(
        page: Int,
        name: String,
        type: String,
        dimension: String,
    ): Location {
        val locationApi = apiService.getAllLocation(page, name, type, dimension)
        val listLocations = mapper.mapLocationResponseForLocation(locationApi)
        locationDao.insertLocation(mapper.mapListResultResponseForListDb(listLocations.results))
        return listLocations
    }

    override suspend fun insertLocation(list: List<LocationResult>) {
        locationDao.insertLocation(mapper.mapListResultResponseForListDb(list))
    }

    override suspend fun getListLocationsDb(
                                    offset: Int,
                                    limit: Int,
                                    name: String,
                                    type: String,
                                    dimension: String
    ): List<LocationResult> {
            return locationDao.getAllLocationPage(offset, limit, name, type, dimension).map(mapper::mapLocationResultDbForLocationResult)
    }

    override fun getDetailCharacter(id: String): Observable<List<CharacterResult>> {
        return apiService.getDetailCharacter(id)
    }

    override fun getDetailLocation(name: String): Observable<Location> {
        return apiService.getDetailLocation(name)
    }
}