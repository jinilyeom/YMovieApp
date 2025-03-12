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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var homeRequestParam = MutableStateFlow(HomeRequestParam())

    val nowPlayingMoviesUiState: StateFlow<HomeUiState> = homeRequestParam.flatMapLatest { param ->
        movieRepository.fetchNowPlayingMovies(param.language, param.page, param.region)
            .catch { e ->
                HomeUiState.Failure(Exception(e))
            }
            .map { data ->
                HomeUiState.Success(data)
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    val popularMoviesUiState: StateFlow<HomeUiState> = homeRequestParam.flatMapLatest { param ->
        movieRepository.fetchPopularMovies(param.language, param.page, param.region)
            .catch { e ->
                HomeUiState.Failure(Exception(e))
            }
            .map { data ->
                HomeUiState.Success(data)
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    val topRatedMoviesUiState: StateFlow<HomeUiState> = homeRequestParam.flatMapLatest { param ->
        movieRepository.fetchTopRatedMovies(param.language, param.page, param.region)
            .catch { e ->
                HomeUiState.Failure(Exception(e))
            }
            .map { data ->
                HomeUiState.Success(data)
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    val upcomingMoviesUiState: StateFlow<HomeUiState> = homeRequestParam.flatMapLatest { param ->
        movieRepository.fetchUpcomingMovies(param.language, param.page, param.region)
            .catch { e ->
                HomeUiState.Failure(Exception(e))
            }
            .map { data ->
                HomeUiState.Success(data)
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