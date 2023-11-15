package com.ymovie.app.data.source

import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.network.service.MovieService

class RemoteMovieDataSource(private val service: MovieService) {
    suspend fun fetchNowPlayingMovies(language: String, page: Int, region: String): MovieList {
        return service.fetchNowPlayingMovies(language, page, region)
    }

    suspend fun fetchPopularMovies(language: String, page: Int, region: String): MovieList {
        return service.fetchPopularMovies(language, page, region)
    }

    suspend fun fetchTopRatedMovies(language: String, page: Int, region: String): MovieList {
        return service.fetchTopRatedMovies(language, page, region)
    }

    suspend fun fetchUpcomingMovies(language: String, page: Int, region: String): MovieList {
        return service.fetchUpcomingMovies(language, page, region)
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