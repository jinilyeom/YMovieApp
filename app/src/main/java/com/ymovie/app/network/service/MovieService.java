package com.ymovie.app.network.service;

import com.ymovie.app.data.model.movie.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/top_rated")
    Call<MovieList> fetchTopRatedMovies(
            @Query("language") String language,
            @Query("page") int page,
            @Query("region") String region
    );

    @GET("search/movie")
    Call<MovieList> searchMovie(
            @Query("query") String query,
            @Query("include_adult") boolean includeAdult,
            @Query("language") String language,
            @Query("primary_release_year") String primaryReleaseYear,
            @Query("page") int page,
            @Query("region") String region,
            @Query("year") String year
    );
}
