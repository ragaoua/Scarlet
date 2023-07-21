package com.example.scarlet.core.util

sealed class Resource<T>(val data: T? = null, val error: Any? = null)  {

    class Success<T>(data: T?): Resource<T>(data)

    class Error<K, T>(
        error: K,
        data: T? = null
    ): Resource<T>(data, error)

}