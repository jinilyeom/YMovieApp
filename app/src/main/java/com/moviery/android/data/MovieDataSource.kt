package com.moviery.android.data

import com.moviery.android.data.model.movie.Credit
import com.moviery.android.data.model.movie.MovieDetail
import com.moviery.android.data.model.movie.MovieList
import kotlinx.coroutines.flow.Flow

interface MovieDataSource {
    suspend fun fetchNowPlayingMovies(language: String, page: Int, region: String): Flow<MovieList>
    suspend fun fetchPopularMovies(language: String, page: Int, region: String): Flow<MovieList>
    suspend fun fetchTopRatedMovies(language: String, page: Int, region: String): Flow<MovieList>
    suspend fun fetchUpcomingMovies(language: String, page: Int, region: String): Flow<MovieList>
    suspend fun searchMovie(
        query: String,
        includeAdult: Boolean,
        language: String,
        primaryReleaseYear: String,
        page: Int,
        region: String,
        year: String
    ): Flow<MovieList>
    suspend fun fetchMovieDetails(movieId: Int, language: String): Flow<MovieDetail>
    suspend fun fetchCredits(movieId: Int, language: String): Flow<Credit>
}