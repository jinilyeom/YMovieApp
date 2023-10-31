package com.ymovie.app.data;

import com.ymovie.app.data.model.movie.MovieList;
import com.ymovie.app.data.source.RemoteMovieDataSource;

public class MovieRepository {
    private final RemoteMovieDataSource remoteMovieDataSource;

    public MovieRepository(RemoteMovieDataSource remoteMovieDataSource) {
        this.remoteMovieDataSource = remoteMovieDataSource;
    }

    public void fetchTopRatedMovies(String language, int page, String region, ResponseCallback<MovieList> responseCallback) {
        remoteMovieDataSource.fetchTopRatedMovies(language, page, region, new ResponseCallback<MovieList>() {
            @Override
            public void onSuccess(MovieList data) {
                responseCallback.onSuccess(data);
            }

            @Override
            public void onFailure(Throwable t) {
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
        remoteMovieDataSource.searchMovie(
                query, includeAdult, language, primaryReleaseYear, page, region, year,
                new ResponseCallback<MovieList>() {
                    @Override
                    public void onSuccess(MovieList data) {
                        responseCallback.onSuccess(data);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        responseCallback.onFailure(t);
                    }
                }
        );
    }
}
