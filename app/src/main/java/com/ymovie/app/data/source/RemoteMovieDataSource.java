package com.ymovie.app.data.source;

import com.ymovie.app.data.ResponseCallback;
import com.ymovie.app.data.model.movie.MovieList;
import com.ymovie.app.network.service.MovieService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteMovieDataSource {
    private final MovieService service;

    public RemoteMovieDataSource(MovieService service) {
        this.service = service;
    }

    public void fetchTopRatedMovies(String language, int page, String region, ResponseCallback<MovieList> responseCallback) {
        Call<MovieList> call = service.fetchTopRatedMovies(language, page, region);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                responseCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                responseCallback.onFailure(t);
            }
        });
    }

    public void searchMovie(
            String query,
            boolean includeAdult,
            String language,
            String primaryReleaseYear,
            int page,
            String region,
            String year,
            ResponseCallback<MovieList> responseCallback
    ) {
        Call<MovieList> call = service.searchMovie(query, includeAdult, language, primaryReleaseYear, page, region, year);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                responseCallback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                responseCallback.onFailure(t);
            }
        });
    }
}
