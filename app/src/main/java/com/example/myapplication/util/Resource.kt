package com.example.myapplication.util

import com.example.myapplication.domain.enums.DataError

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(val error: DataError) : Resource<T>()
    class Loading<T>() : Resource<T>(null)
}