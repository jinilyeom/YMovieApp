package com.ymovie.app.ui.search

import com.ymovie.app.data.model.movie.MovieList
import java.lang.Exception

sealed class SearchUiState {
    object Loading : SearchUiState()
    data class Success(val data: MovieList) : SearchUiState()
    data class Failure(val exception: Exception) : SearchUiState()
}