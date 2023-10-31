package com.ymovie.app.network.service

import com.ymovie.app.data.model.movie.MovieList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    @GET("movie/top_rated")
    fun fetchTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): Call<MovieList>

    @GET("search/movie")
    fun searchMovie(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("primary_release_year") primaryReleaseYear: String,
        @Query("page") page: Int,
        @Query("region") region: String,
        @Query("year") year: String
    ): Call<MovieList>
}