package com.example.rickyandmorty.data.api.response.location


data class LocationResponse (
    val info: LocationInfoResponse?,
    val results: List<LocationResultResponse>
)