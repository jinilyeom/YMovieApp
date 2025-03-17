package com.ymovie.app.ui.search

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ymovie.app.R
import com.ymovie.app.data.model.SearchRequestParam
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.network.NetworkConstants

private const val TAG = "SearchScreen"
private const val DEFAULT_PAGE = 1

@Composable
fun SearchScreen(searchViewModel: SearchViewModel) {
    val searchResultMovies by searchViewModel.searchResultMovies.collectAsStateWithLifecycle()
    val searchResultUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle()
    var searchQuery by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp)

    Column {
        SearchBar(
            searchQuery,
            onSearchQueryChanged = { searchQuery = it },
            onSearchTriggered = {
                searchViewModel.clearMovies()
                searchViewModel.setSearchRequestParam(
                    SearchRequestParam(query = searchQuery, page = DEFAULT_PAGE)
                )
                keyboardController?.hide()
            },
            modifier
        )
        Spacer(modifier = Modifier.height(8.dp))
        when (searchResultUiState) {
            is SearchUiState.Loading -> {}
            is SearchUiState.Success -> {
                searchViewModel.addMovies((searchResultUiState as SearchUiState.Success).data.movies ?: emptyList())

                SearchResultList(
                    searchQuery,
                    searchResultMovies,
                    (searchResultUiState as SearchUiState.Success).data.page,
                    (searchResultUiState as SearchUiState.Success).data.totalPage,
                    onSearchRequestParamChanged = {
                        searchViewModel.setSearchRequestParam(it)
                    },
                    modifier
                )
            }
            is SearchUiState.Failure -> {
                Log.e(TAG, "${(searchResultUiState as SearchUiState.Failure).exception.message}")
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    modifier: Modifier
) {
    TextField(
        value = searchQuery,
        onValueChange = { onSearchQueryChanged(it) },
        modifier = modifier,
        placeholder = { Text(stringResource(id = R.string.hint_search)) },
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = null)
        },
        trailingIcon = {
            if (searchQuery.isEmpty()) return@TextField

            IconButton(onClick = { onSearchQueryChanged("") }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = null)
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                if (searchQuery.isBlank()) return@KeyboardActions

                onSearchTriggered()
            }
        ),
        maxLines = 1,
        shape = RoundedCornerShape(32.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun SearchResultList(
    searchQuery: String,
    searchResultMovies: List<Movie>,
    page: Int,
    totalPage: Int,
    onSearchRequestParamChanged: (SearchRequestParam) -> Unit,
    modifier: Modifier
) {
    val listState = rememberLazyListState()
    val isLoading by remember { derivedStateOf { listState.isLastVisibleItem() } }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            onSearchRequestParamChanged(
                SearchRequestParam(query = searchQuery, page = page + 1)
            )
        }
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(searchResultMovies.size) { index ->
            SearchResultListItem(searchResultMovies[index], modifier)
        }
    }
}

@Composable
private fun SearchResultListItem(movie: Movie, modifier: Modifier) {
    Card(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = NetworkConstants.IMAGE_BASE_URL_W200 + movie.posterPath,
                contentDescription = null,
                modifier = Modifier.size(100.dp, 147.dp)
            )
            Column(modifier = modifier) {
                Text(
                    text = movie.originalTitle,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = movie.releaseDate, fontSize = 14.sp)
                Text(
                    text = stringResource(
                        id = R.string.label_number_with_percent, (movie.voteAverage * 10).toInt()
                    ),
                    fontSize = 14.sp
                )
            }
        }
    }
}

private fun LazyListState.isLastVisibleItem(): Boolean {
    if (layoutInfo.totalItemsCount == 0) return false

    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

    return lastVisibleIndex + 1 >= layoutInfo.totalItemsCount
}