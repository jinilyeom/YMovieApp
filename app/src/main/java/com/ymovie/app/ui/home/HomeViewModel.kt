package com.ymovie.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.model.movie.MovieList
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _homeDataLiveData: MutableLiveData<ArrayList<NetworkResponse<MovieList>>> = MutableLiveData()
    val homeDataLiveData: LiveData<ArrayList<NetworkResponse<MovieList>>> get() = _homeDataLiveData

    fun fetchHomeData(language: String, page: Int, region: String) {
        viewModelScope.launch {
            val job1 = async {
                movieRepository.fetchNowPlayingMovies(language, page, region)
            }

            val job2 = async {
                movieRepository.fetchPopularMovies(language, page, region)
            }

            val job3 = async {
                movieRepository.fetchTopRatedMovies(language, page, region)
            }

            val job4 = async {
                movieRepository.fetchUpcomingMovies(language, page, region)
            }

            _homeDataLiveData.value = arrayListOf(job1.await(), job2.await(), job3.await(), job4.await())
        }
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