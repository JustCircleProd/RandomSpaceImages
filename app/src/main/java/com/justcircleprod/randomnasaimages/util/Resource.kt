package com.justcircleprod.randomnasaimages.util

sealed class Resource<T>(val data: T? = null, val error: Boolean = false) {
    class Success<T>(data: T) : Resource<T>(data = data)
    class Error<T>(error: Boolean) : Resource<T>(error = error)
}
