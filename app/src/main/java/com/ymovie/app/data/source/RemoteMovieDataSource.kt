package com.ymovie.app.data.source

import com.ymovie.app.data.ResponseCallback
import com.ymovie.app.data.model.movie.MovieList
import com.ymovie.app.network.service.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteMovieDataSource(private val service: MovieService) {
    fun fetchTopRatedMovies(
        language: String,
        page: Int,
        region: String,
        responseCallback: ResponseCallback<MovieList>
    ) {
        val call = service.fetchTopRatedMovies(language, page, region)
        call.enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                responseCallback.onSuccess(response.body() ?: MovieList(1, ArrayList(), 0, 0))
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                responseCallback.onFailure(t)
            }
        })
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
        val call = service.searchMovie(
            query,
            includeAdult,
            language,
            primaryReleaseYear,
            page,
            region,
            year
        )
        call.enqueue(object : Callback<MovieList> {
            override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                responseCallback.onSuccess(response.body() ?: MovieList(1, ArrayList(), 0, 0))
            }

            override fun onFailure(call: Call<MovieList>, t: Throwable) {
                responseCallback.onFailure(t)
            }
        })
    }
}