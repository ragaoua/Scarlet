package com.example.scarlet.core.util

import androidx.annotation.StringRes

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val data: T? = null, @StringRes val errorResId: Int? = null)  {

    class Success<T>(data: T? = null): Resource<T>(data)

    class Error<T>(
        @StringRes error: Int,
        data: T? = null
    ): Resource<T>(data, error)

}