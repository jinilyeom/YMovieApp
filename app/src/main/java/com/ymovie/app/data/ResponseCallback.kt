package com.ymovie.app.data

interface ResponseCallback<T> {
    fun onSuccess(data: T)
    fun onFailure(t: Throwable)
}