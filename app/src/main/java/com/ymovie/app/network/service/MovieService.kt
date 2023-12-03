package com.ymovie.app.network.service

import com.ymovie.app.data.model.movie.Credit
import com.ymovie.app.data.model.movie.MovieDetail
import com.ymovie.app.data.model.movie.MovieList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/now_playing")
    suspend fun fetchNowPlayingMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): MovieList

    @GET("movie/popular")
    suspend fun fetchPopularMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): MovieList

    @GET("movie/top_rated")
    suspend fun fetchTopRatedMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): MovieList

    @GET("movie/upcoming")
    suspend fun fetchUpcomingMovies(
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("region") region: String
    ): MovieList

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean,
        @Query("language") language: String,
        @Query("primary_release_year") primaryReleaseYear: String,
        @Query("page") page: Int,
        @Query("region") region: String,
        @Query("year") year: String
    ): MovieList

    @GET("movie/{movie_id}")
    suspend fun fetchMovieDetails(@Path("movie_id") movieId: Int): MovieDetail

    @GET("movie/{movie_id}/credits")
    suspend fun fetchCredits(@Path("movie_id") movieId: Int): Credit
}