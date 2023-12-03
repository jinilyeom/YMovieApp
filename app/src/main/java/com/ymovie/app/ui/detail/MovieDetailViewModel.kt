package com.ymovie.app.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.model.movie.Credit
import com.ymovie.app.data.model.movie.MovieDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MovieDetailViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _detailBasicsLiveData: MutableLiveData<NetworkResponse<MovieDetail>> = MutableLiveData()
    val detailBasicsLiveData: LiveData<NetworkResponse<MovieDetail>> get() = _detailBasicsLiveData
    private var _creditsLiveData: MutableLiveData<NetworkResponse<Credit>> = MutableLiveData()
    val creditsLiveData: LiveData<NetworkResponse<Credit>> get() = _creditsLiveData

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun fetchMovieDetails(movieId: Int) {
        scope.launch {
            val job = async {
                movieRepository.fetchMovieDetails(movieId)
            }

            _detailBasicsLiveData.value = job.await()
        }
    }

    fun fetchCredits(movieId: Int) {
        scope.launch {
            val job = async {
                movieRepository.fetchCredits(movieId)
            }

            _creditsLiveData.value = job.await()
        }
    }

    override fun onCleared() {
        super.onCleared()

        scope.cancel()
    }
}

class MovieDetailViewModelFactory(private val movieRepository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieDetailViewModel::class.java)) {
            return MovieDetailViewModel(movieRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}