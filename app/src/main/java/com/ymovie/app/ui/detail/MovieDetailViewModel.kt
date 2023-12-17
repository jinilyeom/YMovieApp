package com.ymovie.app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.model.movie.Credit
import com.ymovie.app.data.model.movie.MovieDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class MovieDetailViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var movieId = MutableStateFlow(-1)

    val movieDetail: StateFlow<NetworkResponse<MovieDetail>> = movieId.flatMapLatest { id ->
        movieRepository.fetchMovieDetails(id)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = NetworkResponse.Loading
    )

    val movieCredit: StateFlow<NetworkResponse<Credit>> = movieId.flatMapLatest { id ->
        movieRepository.fetchCredits(id)
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = NetworkResponse.Loading
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