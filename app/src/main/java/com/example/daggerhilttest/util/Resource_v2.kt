package com.example.daggerhilttest.util

sealed class Resource_v2<T> {

    data class Success<T>(val data: T) : Resource_v2<T>()

    data class Error<T>(val message: String?) : Resource_v2<T>()

    class Loading<T> : Resource_v2<T>()

}
