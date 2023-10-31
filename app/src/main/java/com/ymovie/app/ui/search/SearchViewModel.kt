package com.ymovie.app.ui.search;

import com.ymovie.app.data.MovieRepository;
import com.ymovie.app.data.ResponseCallback;
import com.ymovie.app.data.model.movie.MovieList;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class SearchViewModel extends ViewModel {
    private MutableLiveData<MovieList> searchResultLiveData;
    private final MovieRepository movieRepository;

    public SearchViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public MutableLiveData<MovieList> getSearchResultLiveData() {
        searchResultLiveData = new MutableLiveData<>();

        return searchResultLiveData;
    }

    public void searchMovie(
            String query,
            boolean includeAdult,
            String language,
            String primaryReleaseYear,
            int page,
            String region,
            String year
    ) {
        movieRepository.searchMovie(
                query, includeAdult, language, primaryReleaseYear, page, region, year,
                new ResponseCallback<MovieList>() {
                    @Override
                    public void onSuccess(MovieList data) {
                        searchResultLiveData.setValue(data);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        searchResultLiveData.setValue(null);
                    }
                }
        );
    }
}

class SearchViewModelFactory implements ViewModelProvider.Factory {
    private final MovieRepository movieRepository;

    public SearchViewModelFactory(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SearchViewModel.class)) {
            return (T) new SearchViewModel(movieRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
