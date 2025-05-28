package com.rabadiya.base.common

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val status: Int? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null, status: Int? = null) : Resource<T>(data, message, status)
    class Loading<T> : Resource<T>()
}