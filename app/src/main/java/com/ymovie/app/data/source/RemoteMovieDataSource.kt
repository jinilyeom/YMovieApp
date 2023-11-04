package com.ymovie.app.data.source

import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.network.service.MovieService

class RemoteMovieDataSource(private val service: MovieService) {
    suspend fun fetchTopRatedMovies(language: String, page: Int, region: String): MovieList {
        return service.fetchTopRatedMovies(language, page, region)
    }

    suspend fun searchMovie(
        query: String,
        includeAdult: Boolean,
        language: String,
        primaryReleaseYear: String,
        page: Int,
        region: String,
        year: String
    ): MovieList {
        return service.searchMovie(query, includeAdult, language, primaryReleaseYear, page, region, year)
    }
}