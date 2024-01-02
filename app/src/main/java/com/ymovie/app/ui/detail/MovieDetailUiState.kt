package com.ymovie.app.ui.detail

import com.ymovie.app.data.model.movie.MovieDetail
import java.lang.Exception

sealed class MovieDetailUiState {
    object Loading : MovieDetailUiState()
    data class Success(val data: MovieDetail) : MovieDetailUiState()
    data class Failure(val exception: Exception) : MovieDetailUiState()
}