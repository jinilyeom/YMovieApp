package com.ymovie.app.data.model

data class HomeRequestParam(
    var language: String = "",
    var page: Int = 1,
    var region: String = ""
)