package com.ymovie.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.model.movie.MovieList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _movieLiveData: MutableLiveData<MovieList> = MutableLiveData()
    val movieLiveData: LiveData<MovieList> get() = _movieLiveData

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun fetchTopRatedMovies(language: String, page: Int, region: String) {
        scope.launch {
            val networkResponse = movieRepository.fetchTopRatedMovies(language, page, region)

            when (networkResponse) {
                is NetworkResponse.Success -> {
                    _movieLiveData.value = networkResponse.data
                }

                is NetworkResponse.Failure -> {
                    _movieLiveData.value = null
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        scope.cancel()
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