package com.moviery.android.data.model

data class HomeReqParam(
    var language: String = "",
    var page: Int = 1,
    var region: String = ""
)