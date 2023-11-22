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
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _searchResultLiveData: MutableLiveData<NetworkResponse<MovieList>> = MutableLiveData()
    val searchResultLiveData: LiveData<NetworkResponse<MovieList>> get() = _searchResultLiveData

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
            val job = async {
                movieRepository.searchMovie(
                    query, includeAdult, language, primaryReleaseYear, page, region, year
                )
            }

            _searchResultLiveData.value = job.await()
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