package com.ymovie.app.data

import com.ymovie.app.data.model.movie.Credit
import com.ymovie.app.data.model.movie.MovieDetail
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.data.source.RemoteMovieDataSource
import com.ymovie.app.ui.home.HomeViewType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieRepository(private val remoteMovieDataSource: RemoteMovieDataSource) {
    suspend fun fetchNowPlayingMovies(language: String, page: Int, region: String): NetworkResponse<MovieList> {
        return try {
            val networkResponse = remoteMovieDataSource.fetchNowPlayingMovies(language, page, region).apply {
                this.header = ""
                this.viewType = HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal
            }

            NetworkResponse.Success(networkResponse)
        } catch (e: Exception) {
            NetworkResponse.Failure(e)
        }
    }

    suspend fun fetchPopularMovies(language: String, page: Int, region: String): NetworkResponse<MovieList> {
        return try {
            val networkResponse = remoteMovieDataSource.fetchPopularMovies(language, page, region).apply {
                this.header = "Popular"
                this.viewType = HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
            }

            NetworkResponse.Success(networkResponse)
        } catch (e: Exception) {
            NetworkResponse.Failure(e)
        }
    }

    suspend fun fetchTopRatedMovies(language: String, page: Int, region: String): NetworkResponse<MovieList> {
        return try {
            val networkResponse = remoteMovieDataSource.fetchTopRatedMovies(language, page, region).apply {
                this.header = "Top Rated"
                this.viewType = HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
            }

            NetworkResponse.Success(networkResponse)
        } catch (e: Exception) {
            NetworkResponse.Failure(e)
        }
    }

    suspend fun fetchUpcomingMovies(language: String, page: Int, region: String): NetworkResponse<MovieList> {
        return try {
            val networkResponse = remoteMovieDataSource.fetchUpcomingMovies(language, page, region).apply {
                this.header = "Upcoming"
                this.viewType = HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
            }

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
    ): Flow<NetworkResponse<MovieList>> {
        return flow {
            try {
                val networkResponse = remoteMovieDataSource.searchMovie(
                    query, includeAdult, language, primaryReleaseYear, page, region, year
                )

                emit(NetworkResponse.Success(networkResponse))
            } catch (e: Exception) {
                emit(NetworkResponse.Failure(e))
            }
        }
    }

    suspend fun fetchMovieDetails(movieId: Int): Flow<NetworkResponse<MovieDetail>> {
        return flow {
            try {
                val networkResponse = remoteMovieDataSource.fetchMovieDetails(movieId)

                emit(NetworkResponse.Success(networkResponse))
            } catch (e: Exception) {
                emit(NetworkResponse.Failure(e))
            }
        }
    }

    suspend fun fetchCredits(movieId: Int): Flow<NetworkResponse<Credit>> {
        return flow {
            try {
                val networkResponse = remoteMovieDataSource.fetchCredits(movieId)

                emit(NetworkResponse.Success(networkResponse))
            } catch (e: Exception) {
                emit(NetworkResponse.Failure(e))
            }
        }
    }
}