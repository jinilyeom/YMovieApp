package com.moviery.android.ui.search

import com.moviery.android.data.model.movie.MovieList
import java.lang.Exception

sealed class SearchUiState {
    object Loading : SearchUiState()
    data class Success(val data: MovieList) : SearchUiState()
    data class Failure(val exception: Exception) : SearchUiState()
}