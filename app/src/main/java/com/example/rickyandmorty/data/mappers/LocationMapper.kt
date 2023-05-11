package com.example.rickyandmorty.data.mappers


import com.example.rickyandmorty.data.api.response.location.LocationInfoResponse
import com.example.rickyandmorty.data.api.response.location.LocationResponse
import com.example.rickyandmorty.data.api.response.location.LocationResultResponse
import com.example.rickyandmorty.data.db.entity.location.LocationDbModel
import com.example.rickyandmorty.domain.models.locations.Location
import com.example.rickyandmorty.domain.models.locations.LocationInfo
import com.example.rickyandmorty.domain.models.locations.LocationResult
import javax.inject.Inject

class LocationMapper @Inject constructor() {
    private fun mapInfoResponseForInfo(infoResponse: LocationInfoResponse?) = LocationInfo(
        count = infoResponse?.count ?: ZERO_NUMBER,
        next = infoResponse?.next ?: EMPTY_STRING,
        pages = infoResponse?.pages ?: ZERO_NUMBER,
        prev = infoResponse?.prev ?: EMPTY_STRING,
    )

    private fun mapResultsResponseForResults(resultResponse: LocationResultResponse?) = LocationResult(
        created = resultResponse?.created ?: EMPTY_STRING,
        dimension = resultResponse?.dimension ?: EMPTY_STRING,
        id = resultResponse?.id ?: ZERO_NUMBER,
        name = resultResponse?.name ?: EMPTY_STRING,
        residents = resultResponse?.residents ?: emptyList(),
        type = resultResponse?.type ?: EMPTY_STRING,
        url = resultResponse?.url ?: EMPTY_STRING
    )

    fun mapListResultsResponseForListResults(list: List<LocationResultResponse>) = list.map {
        mapResultsResponseForResults(it)
    }

    fun mapLocationResponseForLocation(locationResponse: LocationResponse) = Location(
        info = mapInfoResponseForInfo(locationResponse.info),
        results = mapListResultsResponseForListResults(locationResponse.results)
    )

    private fun mapLocationResultResponseForLocationResultDb(locationResult: LocationResult): LocationDbModel {
        return LocationDbModel (
            created = locationResult.created,
            dimension = locationResult.dimension,
            id = locationResult.id,
            name = locationResult.name,
            type = locationResult.type,
            url = locationResult.url,
        )
    }


    fun mapListResultResponseForListDb(list: List<LocationResult>) = list.map {
        mapLocationResultResponseForLocationResultDb(it)
    }

    fun mapLocationResultDbForLocationResult(locationResult: LocationDbModel): LocationResult{
        return LocationResult(
            created = locationResult.created,
            dimension = locationResult.dimension,
            id = locationResult.id,
            name = locationResult.name,
            residents = emptyList(),
            type = locationResult.type,
            url = locationResult.url
        )
    }
    companion object {
        private const val EMPTY_STRING = ""
        private const val ZERO_NUMBER = 0
    }
}