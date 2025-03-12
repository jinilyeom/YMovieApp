package com.ymovie.app.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ymovie.app.R
import com.ymovie.app.data.model.movie.Movie
import com.ymovie.app.network.NetworkConstants

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val nowPlayingMoviesUiState by homeViewModel.nowPlayingMoviesUiState.collectAsStateWithLifecycle()
    val popularMoviesUiState by homeViewModel.popularMoviesUiState.collectAsStateWithLifecycle()
    val topRatedMoviesUiState by homeViewModel.topRatedMoviesUiState.collectAsStateWithLifecycle()
    val upcomingMoviesUiState by homeViewModel.upcomingMoviesUiState.collectAsStateWithLifecycle()

    LazyColumn(verticalArrangement = Arrangement.spacedBy(70.dp)) {
        item {
            when (nowPlayingMoviesUiState) {
                is HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HomeHorizontalPager(
                        (nowPlayingMoviesUiState as HomeUiState.Success).data.movies ?: emptyList()
                    )
                }
                is HomeUiState.Failure -> {
                    Log.e(TAG, "${(nowPlayingMoviesUiState as HomeUiState.Failure).exception.message}")
                }
            }
        }

        item {
            when (popularMoviesUiState) {
                is HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HomeHorizontalList(
                        (popularMoviesUiState as HomeUiState.Success).data.header,
                        (popularMoviesUiState as HomeUiState.Success).data.movies ?: emptyList()
                    )
                }
                is HomeUiState.Failure -> {
                    Log.e(TAG, "${(popularMoviesUiState as HomeUiState.Failure).exception.message}")
                }
            }
        }

        item {
            when (topRatedMoviesUiState) {
                is HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HomeHorizontalList(
                        (topRatedMoviesUiState as HomeUiState.Success).data.header,
                        (topRatedMoviesUiState as HomeUiState.Success).data.movies ?: emptyList()
                    )
                }
                is HomeUiState.Failure -> {
                    Log.e(TAG, "${(topRatedMoviesUiState as HomeUiState.Failure).exception.message}")
                }
            }
        }

        item {
            when (upcomingMoviesUiState) {
                is HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HomeHorizontalList(
                        (upcomingMoviesUiState as HomeUiState.Success).data.header,
                        (upcomingMoviesUiState as HomeUiState.Success).data.movies ?: emptyList()
                    )
                }
                is HomeUiState.Failure -> {
                    Log.e(TAG, "${(upcomingMoviesUiState as HomeUiState.Failure).exception.message}")
                }
            }
        }
    }
}

@Composable
fun HomeHorizontalPager(
    movies: List<Movie>,
) {
    val pagerState = rememberPagerState {
        movies.size
    }

    HorizontalPager(state = pagerState, pageSize = PageSize.Fill) { page ->
        Card(shape = RoundedCornerShape(0.dp), colors = CardDefaults.cardColors(containerColor = Color.Gray)) {
            AsyncImage(
                model = NetworkConstants.IMAGE_BASE_URL_W500 + movies[page].backdropPath,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(270.dp)
            )
            Row {
                AsyncImage(
                    model = NetworkConstants.IMAGE_BASE_URL_W200 + movies[page].posterPath,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp, 150.dp)
                )
                Column {
                    Text(
                        text = movies[page].originalTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = movies[page].releaseDate, fontSize = 14.sp)
                    Text(
                        text = stringResource(
                            id = R.string.label_number_with_percent, (movies[page].voteAverage * 10).toInt()
                        ),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun HomeHorizontalList(
    headerText: String, movies: List<Movie>
) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = headerText,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        items(movies.size) { index ->
            Card(modifier = Modifier.width(150.dp)) {
                AsyncImage(
                    model = NetworkConstants.IMAGE_BASE_URL_W200 + movies[index].posterPath,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .wrapContentHeight()
                )
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = movies[index].originalTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = movies[index].releaseDate,
                        fontSize = 14.sp,
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}