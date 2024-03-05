package com.ymovie.app.data

import com.ymovie.app.data.model.movie.Credit
import com.ymovie.app.data.model.movie.MovieDetail
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.data.source.MovieRemoteDataSource
import com.ymovie.app.ui.home.HomeViewType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(private val movieRemoteDataSource: MovieRemoteDataSource) {
    suspend fun fetchNowPlayingMovies(language: String, page: Int, region: String): Flow<MovieList> {
        return movieRemoteDataSource.fetchNowPlayingMovies(language, page, region).map {
            it.header = ""
            it.viewType = HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal
            it
        }
    }

    suspend fun fetchPopularMovies(language: String, page: Int, region: String): Flow<MovieList> {
        return movieRemoteDataSource.fetchPopularMovies(language, page, region).map {
            it.header = "Popular"
            it.viewType = HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
            it
        }
    }

    suspend fun fetchTopRatedMovies(language: String, page: Int, region: String): Flow<MovieList> {
        return movieRemoteDataSource.fetchTopRatedMovies(language, page, region).map {
            it.header = "Top Rated"
            it.viewType = HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
            it
        }
    }

    suspend fun fetchUpcomingMovies(language: String, page: Int, region: String): Flow<MovieList> {
        return movieRemoteDataSource.fetchUpcomingMovies(language, page, region).map {
            it.header = "Upcoming"
            it.viewType = HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
            it
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
    ): Flow<MovieList> {
        return movieRemoteDataSource.searchMovie(
            query, includeAdult, language, primaryReleaseYear, page, region, year
        )
    }

    suspend fun fetchMovieDetails(movieId: Int): Flow<MovieDetail> {
        return movieRemoteDataSource.fetchMovieDetails(movieId)
    }

    suspend fun fetchCredits(movieId: Int): Flow<Credit> {
        return movieRemoteDataSource.fetchCredits(movieId)
    }
}