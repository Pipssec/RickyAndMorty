package com.example.rickyandmorty.domain.models.locations

data class LocationInfo(
    val count: Int,
    val next: String?,
    val pages: Int,
    val prev: String
)
