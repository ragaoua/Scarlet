package com.example.scarlet.core.util

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val data: T? = null, val error: StringResource? = null)  {

    class Success<T>(data: T? = null): Resource<T>(data = data)
    class Error<T>(error: StringResource): Resource<T>(error = error)
}