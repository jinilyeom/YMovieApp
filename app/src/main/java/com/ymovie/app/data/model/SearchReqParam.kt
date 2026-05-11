package com.ymovie.app.data.model

data class SearchReqParam(
    var query: String = "",
    var includeAdult: Boolean = false,
    var language: String = "",
    var primaryReleaseYear: String = "",
    var page: Int = 1,
    var region: String = "",
    var year: String = ""
)