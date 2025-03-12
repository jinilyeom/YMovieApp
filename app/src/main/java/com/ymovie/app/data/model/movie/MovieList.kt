package com.ymovie.app.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

const val DEFAULT_PAGE = 1
const val DEFAULT_TOTAL_PAGE = 1
const val DEFAULT_TOTAL_RESULT = 0
const val DEFAULT_HEADER = ""

data class MovieList(
    @SerializedName("page")
    @Expose
    var page: Int = DEFAULT_PAGE,
    @SerializedName("results")
    @Expose
    var movies: List<Movie>? = emptyList(),
    @SerializedName("total_pages")
    @Expose
    var totalPage: Int = DEFAULT_TOTAL_PAGE,
    @SerializedName("total_results")
    @Expose
    var totalResult: Int = DEFAULT_TOTAL_RESULT,
    var header: String = DEFAULT_HEADER
)