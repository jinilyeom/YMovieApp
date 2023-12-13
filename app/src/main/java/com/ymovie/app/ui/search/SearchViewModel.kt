package com.ymovie.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.model.movie.MovieList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    private var searchRequestParam = MutableStateFlow(SearchRequestParam())

    val searchMovie: StateFlow<NetworkResponse<MovieList>> = searchRequestParam.flatMapLatest {
        movieRepository.searchMovie(
            it.query, it.includeAdult, it.language, it.primaryReleaseYear, it.page, it.region, it.year
        )
    }.stateIn(
        scope = scope,
        started = WhileSubscribed(5000),
        initialValue = NetworkResponse.Loading
    )

    fun setSearchRequestParam(param: SearchRequestParam) {
        searchRequestParam.value = param
    }

    override fun onCleared() {
        super.onCleared()

        scope.cancel()
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