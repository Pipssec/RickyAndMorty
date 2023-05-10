package com.example.rickyandmorty.data.api.response.episode


data class EpisodeResponse(
    val info: EpisodeInfoResponse,
    val results: List<EpisodeResultResponse>
)
