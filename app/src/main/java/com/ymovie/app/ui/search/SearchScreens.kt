package com.ymovie.app.ui.search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
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
fun SearchScreen(searchViewModel: SearchViewModel, onItemClick: (Int) -> Unit) {
    val searchResultUiState by searchViewModel.searchUiState.collectAsStateWithLifecycle()
    val searchQuery by searchViewModel.searchQuery.collectAsStateWithLifecycle()
    val searchRequestParam by searchViewModel.searchRequestParam.collectAsStateWithLifecycle()

    SearchScreen(
        searchViewModel = searchViewModel,
        searchResultUiState = searchResultUiState,
        searchQuery = searchQuery,
        searchRequestParam = searchRequestParam,
        onItemClick = onItemClick
    )
}

@Composable
private fun SearchScreen(
    searchViewModel: SearchViewModel,
    searchResultUiState: SearchUiState,
    searchQuery: String,
    searchRequestParam: SearchRequestParam,
    onItemClick: (Int) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        SearchBar(
            searchQuery,
            onSearchQueryChanged = {
                searchViewModel.setSearchQuery(it)
            },
            onSearchTriggered = {
                searchViewModel.clearMovies()
                searchViewModel.setSearchRequestParam(
                    SearchRequestParam(query = searchQuery, page = DEFAULT_PAGE)
                )
                keyboardController?.hide()
            }
        )
        Spacer(modifier = Modifier.height(8.dp))
        when (searchResultUiState) {
            is SearchUiState.Loading -> {}
            is SearchUiState.Success -> {
                SearchResultList(
                    searchResultUiState.data.movies ?: emptyList(),
                    onLoadMore = { isLoading ->
                        if (isLoading && searchResultUiState.data.page <= searchResultUiState.data.totalPage) {
                            searchViewModel.setSearchRequestParam(
                                SearchRequestParam(query = searchRequestParam.query, page = searchResultUiState.data.page + 1)
                            )
                        }
                    },
                    onItemClick
                )
            }
            is SearchUiState.Failure -> {
                Log.e(TAG, "${searchResultUiState.exception.message}")
            }
        }
    }
}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChanged: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    TextField(
        value = searchQuery,
        onValueChange = { onSearchQueryChanged(it) },
        modifier = Modifier.fillMaxWidth(),
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
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color(0xFF2B292D),
            unfocusedContainerColor = Color(0xFF2B292D),
            cursorColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedLeadingIconColor = Color.White,
            unfocusedLeadingIconColor = Color.White,
            focusedTrailingIconColor = Color.White,
            unfocusedTrailingIconColor = Color.White
        )
    )
}

@Composable
private fun SearchResultList(
    searchResultMovies: List<Movie>,
    onLoadMore: (Boolean) -> Unit,
    onItemClick: (Int) -> Unit
) {
    val listState = rememberLazyListState()
    val isLoading by remember { derivedStateOf { listState.isLastVisibleItem() } }

    LaunchedEffect(isLoading) {
        onLoadMore(isLoading)
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(searchResultMovies.size) { index ->
            SearchResultListItem(searchResultMovies[index], onItemClick)
        }
    }
}

@Composable
private fun SearchResultListItem(movie: Movie, onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(147.dp).clickable { onItemClick(movie.id) },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Row {
            Box(
                modifier = Modifier.background(Color(0xFF353438)).width(100.dp).fillMaxHeight()
            ) {
                if (movie.posterPath.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.ic_broken_image_24px),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp).align(Alignment.Center),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                } else {
                    AsyncImage(
                        model = NetworkConstants.IMAGE_BASE_URL_W200 + movie.posterPath,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp), verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = movie.originalTitle,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Text(text = movie.releaseDate, color = Color.White, fontSize = 14.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp, 14.dp),
                        tint = Color(0xFFFF97B7)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(id = R.string.label_number_with_percent, (movie.voteAverage * 10).toInt()),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

private fun LazyListState.isLastVisibleItem(): Boolean {
    if (layoutInfo.totalItemsCount == 0) return false

    val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

    return lastVisibleIndex + 1 >= layoutInfo.totalItemsCount
}