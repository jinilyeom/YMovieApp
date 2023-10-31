package com.ymovie.app.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.ResponseCallback
import com.ymovie.app.data.model.movie.MovieList

class SearchViewModel(private val movieRepository: MovieRepository) : ViewModel() {
    private var _searchResultLiveData: MutableLiveData<MovieList> = MutableLiveData()
    val searchResultLiveData: LiveData<MovieList> get() = _searchResultLiveData

    fun searchMovie(
        query: String,
        includeAdult: Boolean,
        language: String,
        primaryReleaseYear: String,
        page: Int,
        region: String,
        year: String
    ) {
        movieRepository.searchMovie(
            query,
            includeAdult,
            language,
            primaryReleaseYear,
            page,
            region,
            year,
            object : ResponseCallback<MovieList> {
                override fun onSuccess(data: MovieList) {
                    _searchResultLiveData.value = data
                }

                override fun onFailure(t: Throwable) {
                    _searchResultLiveData.value = null
                }
            }
        )
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