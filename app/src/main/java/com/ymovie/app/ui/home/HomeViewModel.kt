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
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _homeDataLiveData: MutableLiveData<ArrayList<MovieList>> = MutableLiveData()
    val homeDataLiveData: LiveData<ArrayList<MovieList>> get() = _homeDataLiveData

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun fetchHomeData(language: String, page: Int, region: String) {
        scope.launch {
            val job1 = async {
                responseResult(
                    movieRepository.fetchNowPlayingMovies(language, page, region),
                    "",
                    HomeViewType.MOVIE_PAGER_HORIZONTAL.ordinal
                )
            }

            val job2 = async {
                responseResult(
                    movieRepository.fetchPopularMovies(language, page, region),
                    "Popular",
                    HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
                )
            }

            val job3 = async {
                responseResult(
                    movieRepository.fetchTopRatedMovies(language, page, region),
                    "Top Rated",
                    HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
                )
            }

            val job4 = async {
                responseResult(
                    movieRepository.fetchUpcomingMovies(language, page, region),
                    "Upcoming",
                    HomeViewType.MOVIE_LIST_HORIZONTAL.ordinal
                )
            }

            _homeDataLiveData.value = arrayListOf(job1.await(), job2.await(), job3.await(), job4.await())
        }
    }

    private suspend fun responseResult(
        data: NetworkResponse<MovieList>,
        header: String,
        viewType: Int
    ): MovieList {
        return data.let { response ->
            when (response) {
                is NetworkResponse.Success -> {
                    response.data.apply {
                        this.header = header
                        this.viewType = viewType
                    }
                }

                is NetworkResponse.Failure -> {
                    MovieList(1, emptyList(), 1, 1, "", 1)
                }
            }
        }
    }

    fun clearHomeDataLiveData() {
        _homeDataLiveData.value = null
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