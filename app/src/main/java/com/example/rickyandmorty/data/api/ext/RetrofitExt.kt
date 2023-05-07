package com.example.rickyandmorty.data.api.ext

import com.example.rickyandmorty.data.api.exception.BackendException
import com.example.rickyandmorty.data.api.response.ErrorResponse
import com.squareup.moshi.Moshi
import retrofit2.HttpException

fun backendException(e: Exception): Exception {
    when (e) {
        is HttpException -> {
            val errorBody = e.response()?.errorBody()?.string()
            return if (errorBody == null) {
                e
            } else {
                val moshi = Moshi.Builder().build()
                val adapter = moshi.adapter(ErrorResponse::class.java)
                val error = runCatching { adapter.fromJson(errorBody) }.getOrNull()
                if (error != null) {
                    BackendException(error.error)
                } else {
                    e
                }
            }
        }
        else -> return e
    }
}