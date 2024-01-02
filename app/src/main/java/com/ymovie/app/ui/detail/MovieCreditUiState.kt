package com.ymovie.app.ui.detail

import com.ymovie.app.data.model.movie.Credit
import java.lang.Exception

sealed class MovieCreditUiState {
    object Loading : MovieCreditUiState()
    data class Success(val data: Credit) : MovieCreditUiState()
    data class Failure(val exception: Exception) : MovieCreditUiState()
}
