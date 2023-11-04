package com.ymovie.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.NetworkResponse
import com.ymovie.app.data.model.movie.MovieList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _searchResultLiveData: MutableLiveData<MovieList> = MutableLiveData()
    val searchResultLiveData: LiveData<MovieList> get() = _searchResultLiveData

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    fun searchMovie(
        query: String,
        includeAdult: Boolean,
        language: String,
        primaryReleaseYear: String,
        page: Int,
        region: String,
        year: String
    ) {
        scope.launch {
            val networkResponse = movieRepository.searchMovie(
                query, includeAdult, language, primaryReleaseYear, page, region, year
            )

            when (networkResponse) {
                is NetworkResponse.Success -> {
                    _searchResultLiveData.value = networkResponse.data
                }

                is NetworkResponse.Failure -> {
                    _searchResultLiveData.value = null
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        scope.cancel()
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