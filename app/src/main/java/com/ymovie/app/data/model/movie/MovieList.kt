package com.ymovie.app.data.model.movie

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MovieList(
    @SerializedName("page")
    @Expose
    var page: Int,
    @SerializedName("results")
    @Expose
    var movies: List<Movie>?,
    @SerializedName("total_pages")
    @Expose
    var totalPage: Int,
    @SerializedName("total_results")
    @Expose
    var totalResult: Int,
    var header: String,
    var viewType: Int
)