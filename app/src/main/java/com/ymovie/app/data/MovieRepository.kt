package com.ymovie.app.data

import com.ymovie.app.data.model.movie.Credit
import com.ymovie.app.data.model.movie.MovieDetail
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.data.source.RemoteMovieDataSource
import com.ymovie.app.ui.home.HomeViewType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepository(private val remoteMovieDataSource: RemoteMovieDataSource) {
    suspend fun fetchNowPlayingMovies(language: String, page: Int, region: String): Flow<MovieList> {
        return remoteMovieDataSource.fetchNowPlayingMovies(language, page, region).map {
            it.header = ""
            it.viewType = HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal
            it
        }
    }

    suspend fun fetchPopularMovies(language: String, page: Int, region: String): Flow<MovieList> {
        return remoteMovieDataSource.fetchPopularMovies(language, page, region).map {
            it.header = "Popular"
            it.viewType = HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
            it
        }
    }

    suspend fun fetchTopRatedMovies(language: String, page: Int, region: String): Flow<MovieList> {
        return remoteMovieDataSource.fetchTopRatedMovies(language, page, region).map {
            it.header = "Top Rated"
            it.viewType = HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
            it
        }
    }

    suspend fun fetchUpcomingMovies(language: String, page: Int, region: String): Flow<MovieList> {
        return remoteMovieDataSource.fetchUpcomingMovies(language, page, region).map {
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
        return remoteMovieDataSource.searchMovie(
            query, includeAdult, language, primaryReleaseYear, page, region, year
        )
    }

    suspend fun fetchMovieDetails(movieId: Int): Flow<MovieDetail> {
        return remoteMovieDataSource.fetchMovieDetails(movieId)
    }

    suspend fun fetchCredits(movieId: Int): Flow<Credit> {
        return remoteMovieDataSource.fetchCredits(movieId)
    }
}