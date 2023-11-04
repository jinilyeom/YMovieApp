package com.ymovie.app.data

import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.data.source.RemoteMovieDataSource

class MovieRepository(private val remoteMovieDataSource: RemoteMovieDataSource) {
    suspend fun fetchTopRatedMovies(language: String, page: Int, region: String): NetworkResponse<MovieList> {
        return try {
            val networkResponse = remoteMovieDataSource.fetchTopRatedMovies(language, page, region)

            NetworkResponse.Success(networkResponse)
        } catch (e: Exception) {
            NetworkResponse.Failure(e)
        }
    }

    suspend fun searchMovie(
        query: String,
        includeAdult: Boolean,
        language: String,
        primaryReleaseYear: String,
        page: Int,
        region: String,
        year: String
    ): NetworkResponse<MovieList> {
        return try {
            val networkResponse = remoteMovieDataSource.searchMovie(
                query, includeAdult, language, primaryReleaseYear, page, region, year
            )

            NetworkResponse.Success(networkResponse)
        } catch (e: Exception) {
            NetworkResponse.Failure(e)
        }
    }
}