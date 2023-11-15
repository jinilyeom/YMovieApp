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
    private var _nowPlayingLiveData: MutableLiveData<MovieList> = MutableLiveData()
    val nowPlayingLiveData: LiveData<MovieList> get() = _nowPlayingLiveData
    private var _popularLiveData: MutableLiveData<MovieList> = MutableLiveData()
    val popularLiveData: LiveData<MovieList> get() = _popularLiveData
    private var _topRatedLiveData: MutableLiveData<MovieList> = MutableLiveData()
    val topRatedLiveData: LiveData<MovieList> get() = _topRatedLiveData
    private var _upcomingLiveData: MutableLiveData<MovieList> = MutableLiveData()
    val upcomingLiveData: LiveData<MovieList> get() = _upcomingLiveData

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun fetchNowPlayingMovies(language: String, page: Int, region: String) {
        scope.launch {
            val networkResponse = movieRepository.fetchNowPlayingMovies(language, page, region)

            when (networkResponse) {
                is NetworkResponse.Success -> {
                    _nowPlayingLiveData.value = networkResponse.data
                }

                is NetworkResponse.Failure -> {
                    _nowPlayingLiveData.value = null
                }
            }
        }
    }

    fun fetchPopularMovies(language: String, page: Int, region: String) {
        scope.launch {
            val networkResponse = movieRepository.fetchPopularMovies(language, page, region)

            when (networkResponse) {
                is NetworkResponse.Success -> {
                    networkResponse.data.header = "Popular"
                    _popularLiveData.value = networkResponse.data
                }

                is NetworkResponse.Failure -> {
                    _popularLiveData.value = null
                }
            }
        }
    }

    fun fetchTopRatedMovies(language: String, page: Int, region: String) {
        scope.launch {
            val networkResponse = movieRepository.fetchTopRatedMovies(language, page, region)

            when (networkResponse) {
                is NetworkResponse.Success -> {
                    networkResponse.data.header = "Top Rated"
                    _topRatedLiveData.value = networkResponse.data
                }

                is NetworkResponse.Failure -> {
                    _topRatedLiveData.value = null
                }
            }
        }
    }

    fun fetchUpcomingMovies(language: String, page: Int, region: String) {
        scope.launch {
            val networkResponse = movieRepository.fetchUpcomingMovies(language, page, region)

            when (networkResponse) {
                is NetworkResponse.Success -> {
                    networkResponse.data.header = "Upcoming"
                    _upcomingLiveData.value = networkResponse.data
                }

                is NetworkResponse.Failure -> {
                    _upcomingLiveData.value = null
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