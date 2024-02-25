package com.ymovie.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.model.HomeRequestParam
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var homeRequestParam = MutableStateFlow(HomeRequestParam())

    val homeData: StateFlow<HomeUiState> = homeRequestParam.flatMapLatest { param ->
        flowOf(
            movieRepository.fetchNowPlayingMovies(param.language, param.page, param.region),
            movieRepository.fetchPopularMovies(param.language, param.page, param.region),
            movieRepository.fetchTopRatedMovies(param.language, param.page, param.region),
            movieRepository.fetchUpcomingMovies(param.language, param.page, param.region)
        ).flatMapConcat {
            it.catch { e ->
                HomeUiState.Failure(Exception(e))
            }.map { data ->
                HomeUiState.Success(data)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    fun setHomeRequestParam(param: HomeRequestParam) {
        homeRequestParam.value = param
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