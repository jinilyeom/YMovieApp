package com.moviery.android.ui.home

import com.moviery.android.data.model.movie.MovieList
import java.lang.Exception

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val data: MovieList) : HomeUiState()
    data class Failure(val exception: Exception) : HomeUiState()
}