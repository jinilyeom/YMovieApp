package com.ymovie.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ymovie.app.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MovieDetailViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var movieId = MutableStateFlow(-1)

    val movieDetail: StateFlow<MovieDetailUiState> = movieId.flatMapLatest { id ->
        movieRepository.fetchMovieDetails(id)
            .catch { e ->
                MovieDetailUiState.Failure(Exception(e))
            }
            .map { detail ->
                MovieDetailUiState.Success(detail)
            }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = MovieDetailUiState.Loading
    )

    val movieCredit: StateFlow<MovieCreditUiState> = movieId.flatMapLatest { id ->
        movieRepository.fetchCredits(id)
            .catch { e ->
                MovieCreditUiState.Failure(Exception(e))
            }
            .map { credit ->
                MovieCreditUiState.Success(credit)
            }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = MovieCreditUiState.Loading
    )

    fun setMovieId(id: Int) {
        movieId.value = id
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