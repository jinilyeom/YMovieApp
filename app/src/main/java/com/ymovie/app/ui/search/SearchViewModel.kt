package com.ymovie.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.model.movie.MovieList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var searchRequestParam = MutableStateFlow(SearchRequestParam())

    val searchMovie: StateFlow<NetworkResponse<MovieList>> = searchRequestParam.flatMapLatest {
        movieRepository.searchMovie(
            it.query, it.includeAdult, it.language, it.primaryReleaseYear, it.page, it.region, it.year
        )
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = NetworkResponse.Loading
    )

    fun setSearchRequestParam(param: SearchRequestParam) {
        searchRequestParam.value = param
    }
}

class SearchViewModelFactory(private val movieRepository: MovieRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(movieRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}