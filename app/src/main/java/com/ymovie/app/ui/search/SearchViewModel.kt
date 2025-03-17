package com.ymovie.app.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.model.SearchRequestParam
import com.ymovie.app.data.model.movie.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private val _searchResultMovies: MutableStateFlow<MutableList<Movie>> = MutableStateFlow(mutableListOf())
    val searchResultMovies: StateFlow<List<Movie>>
        get() = _searchResultMovies
    private var searchRequestParam = MutableStateFlow(SearchRequestParam())

    val searchUiState: StateFlow<SearchUiState> = searchRequestParam.flatMapLatest { param ->
        movieRepository.searchMovie(
            param.query,
            param.includeAdult,
            param.language,
            param.primaryReleaseYear,
            param.page,
            param.region,
            param.year
        )
            .catch { e ->
                SearchUiState.Failure(Exception(e))
            }
            .map { data ->
                SearchUiState.Success(data)
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState.Loading
    )

    fun setSearchRequestParam(param: SearchRequestParam) {
        searchRequestParam.value = param
    }

    fun addMovies(movies: List<Movie>) {
        _searchResultMovies.update {
            it.addAll(movies)
            it
        }
    }

    fun clearMovies() {
        _searchResultMovies.update {
            it.clear()
            it
        }
    }
}

class SearchViewModelFactory(private val movieRepository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(movieRepository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}