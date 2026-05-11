package com.moviery.android.data.source

import com.moviery.android.data.MovieDataSource
import com.moviery.android.data.model.movie.Credit
import com.moviery.android.data.model.movie.MovieDetail
import com.moviery.android.data.model.movie.MovieList
import com.moviery.android.network.service.MovieService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRemoteDataSource(private val service: MovieService) : MovieDataSource {
    override suspend fun fetchNowPlayingMovies(language: String, page: Int, region: String): Flow<MovieList> = flow {
        emit(service.fetchNowPlayingMovies(language, page, region))
    }

    override suspend fun fetchPopularMovies(language: String, page: Int, region: String): Flow<MovieList> = flow {
        emit(service.fetchPopularMovies(language, page, region))
    }

    override suspend fun fetchTopRatedMovies(language: String, page: Int, region: String): Flow<MovieList> = flow {
        emit(service.fetchTopRatedMovies(language, page, region))
    }

    override suspend fun fetchUpcomingMovies(language: String, page: Int, region: String): Flow<MovieList> = flow {
        emit(service.fetchUpcomingMovies(language, page, region))
    }

    override suspend fun searchMovie(
        query: String,
        includeAdult: Boolean,
        language: String,
        primaryReleaseYear: String,
        page: Int,
        region: String,
        year: String
    ): Flow<MovieList> = flow {
        emit(service.searchMovie(query, includeAdult, language, primaryReleaseYear, page, region, year))
    }

    override suspend fun fetchMovieDetails(movieId: Int, language: String): Flow<MovieDetail> = flow {
        emit(service.fetchMovieDetails(movieId, language))
    }

    override suspend fun fetchCredits(movieId: Int, language: String): Flow<Credit> = flow {
        emit(service.fetchCredits(movieId, language))
    }
}