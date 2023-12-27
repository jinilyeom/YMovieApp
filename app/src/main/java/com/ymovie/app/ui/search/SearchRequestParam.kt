package com.ymovie.app.ui.search

data class SearchRequestParam(
    var query: String = "",
    var includeAdult: Boolean = false,
    var language: String = "en-US",
    var primaryReleaseYear: String = "",
    var page: Int = 1,
    var region: String = "",
    var year: String = ""
)