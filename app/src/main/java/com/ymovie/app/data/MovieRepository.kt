package com.ymovie.app.data

import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.data.source.RemoteMovieDataSource

class MovieRepository(private val remoteMovieDataSource: RemoteMovieDataSource) {
    fun fetchTopRatedMovies(
        language: String,
        page: Int,
        region: String,
        responseCallback: ResponseCallback<MovieList>
    ) {
        remoteMovieDataSource.fetchTopRatedMovies(
            language,
            page,
            region,
            object : ResponseCallback<MovieList> {
                override fun onSuccess(data: MovieList) {
                    responseCallback.onSuccess(data)
                }

                override fun onFailure(t: Throwable) {
                    responseCallback.onFailure(t)
                }
            }
        )
    }

    fun searchMovie(
        query: String,
        includeAdult: Boolean,
        language: String,
        primaryReleaseYear: String,
        page: Int,
        region: String,
        year: String,
        responseCallback: ResponseCallback<MovieList>
    ) {
        remoteMovieDataSource.searchMovie(
            query,
            includeAdult,
            language,
            primaryReleaseYear,
            page,
            region,
            year,
            object : ResponseCallback<MovieList> {
                override fun onSuccess(data: MovieList) {
                    responseCallback.onSuccess(data)
                }

                override fun onFailure(t: Throwable) {
                    responseCallback.onFailure(t)
                }
            }
        )
    }
}