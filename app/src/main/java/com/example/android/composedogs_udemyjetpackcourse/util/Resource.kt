package com.example.android.composedogs_udemyjetpackcourse.util

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Loading<T>(val isLoading: Boolean = true): Resource<T>(null)
    class Success<T>(data: T?, message: String? = null): Resource<T>(data, message)
    class Error<T>(message: String, data: T? = null): Resource<T>(data, message)
}