package com.ymovie.app.ui.home;

import com.ymovie.app.data.MovieRepository;
import com.ymovie.app.data.ResponseCallback;
import com.ymovie.app.data.model.movie.MovieList;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<MovieList> movieLiveData;
    private final MovieRepository movieRepository;

    public HomeViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public LiveData<MovieList> getMovieLiveData() {
        movieLiveData = new MutableLiveData<>();

        return movieLiveData;
    }

    public void fetchTopRatedMovies(String language, int page, String region) {
        movieRepository.fetchTopRatedMovies(language, page, region, new ResponseCallback<MovieList>() {
            @Override
            public void onSuccess(MovieList data) {
                movieLiveData.setValue(data);
            }

            @Override
            public void onFailure(Throwable t) {
                movieLiveData.setValue(null);
            }
        });
    }
}

class HomeViewModelFactory implements ViewModelProvider.Factory {
    private final MovieRepository movieRepository;

    public HomeViewModelFactory(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) new HomeViewModel(movieRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
