package com.example.rickyandmorty.data.api.exception

class BackendException(
    override val message: String
) : RuntimeException(message)