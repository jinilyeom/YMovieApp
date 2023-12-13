package com.ymovie.app.data

import java.lang.Exception

sealed class NetworkResponse<out T> {
    object Loading : NetworkResponse<Nothing>()
    data class Success<out T>(val data: T) : NetworkResponse<T>()
    data class Failure(val exception: Exception) : NetworkResponse<Nothing>()
}