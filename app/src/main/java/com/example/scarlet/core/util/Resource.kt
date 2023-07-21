package com.example.scarlet.core.util

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val data: T? = null, val error: Any? = null)  {

    class Success<T>(data: T? = null): Resource<T>(data)

    class Error<K, T>(
        error: K,
        data: T? = null
    ): Resource<T>(data, error)

}