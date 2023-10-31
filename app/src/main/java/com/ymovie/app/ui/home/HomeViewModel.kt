package com.ymovie.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.ResponseCallback
import com.ymovie.app.data.model.movie.MovieList

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _movieLiveData: MutableLiveData<MovieList> = MutableLiveData()
    val movieLiveData: LiveData<MovieList> get() = _movieLiveData

    fun fetchTopRatedMovies(language: String, page: Int, region: String) {
        movieRepository.fetchTopRatedMovies(
            language,
            page,
            region,
            object : ResponseCallback<MovieList> {
                override fun onSuccess(data: MovieList) {
                    _movieLiveData.value = data
                }

                override fun onFailure(t: Throwable) {
                    _movieLiveData.value = null
                }
            }
        )
    }
}

class HomeViewModelFactory(private val movieRepository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(movieRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}