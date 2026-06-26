package com.moviery.android.ui.home

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.moviery.android.R
import com.moviery.android.data.model.movie.Movie
import com.moviery.android.network.NetworkConstants

private const val TAG = "HomeScreen"

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onItemClick: (Int) -> Unit,
    innerPadding: PaddingValues
) {
    val nowPlayingMoviesUiState by homeViewModel.nowPlayingMoviesUiState.collectAsStateWithLifecycle()
    val popularMoviesUiState by homeViewModel.popularMoviesUiState.collectAsStateWithLifecycle()
    val topRatedMoviesUiState by homeViewModel.topRatedMoviesUiState.collectAsStateWithLifecycle()
    val upcomingMoviesUiState by homeViewModel.upcomingMoviesUiState.collectAsStateWithLifecycle()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(innerPadding)
    ) {
        HomeContents(
            nowPlayingMoviesUiState,
            popularMoviesUiState,
            topRatedMoviesUiState,
            upcomingMoviesUiState,
            onItemClick
        )
        HomeAppBar()
    }
}

@Composable
private fun HomeContents(
    nowPlayingMoviesUiState: HomeUiState,
    popularMoviesUiState: HomeUiState,
    topRatedMoviesUiState: HomeUiState,
    upcomingMoviesUiState: HomeUiState,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(48.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height((64.dp - 48.dp)))  // App bar height - Home contents vertical item space
        }
        item {
            when (nowPlayingMoviesUiState) {
                is HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HomeHorizontalPager(
                        nowPlayingMoviesUiState.data.movies ?: emptyList(),
                        onItemClick
                    )
                }
                is HomeUiState.Failure -> {
                    Log.e(TAG, "${nowPlayingMoviesUiState.exception.message}")
                }
            }
        }
        item {
            when (popularMoviesUiState) {
                is HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HomeHorizontalList(
                        popularMoviesUiState.data.header,
                        popularMoviesUiState.data.movies ?: emptyList(),
                        onItemClick
                    )
                }
                is HomeUiState.Failure -> {
                    Log.e(TAG, "${popularMoviesUiState.exception.message}")
                }
            }
        }
        item {
            when (topRatedMoviesUiState) {
                is HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HomeHorizontalList(
                        topRatedMoviesUiState.data.header,
                        topRatedMoviesUiState.data.movies ?: emptyList(),
                        onItemClick
                    )
                }
                is HomeUiState.Failure -> {
                    Log.e(TAG, "${topRatedMoviesUiState.exception.message}")
                }
            }
        }
        item {
            when (upcomingMoviesUiState) {
                is HomeUiState.Loading -> {}
                is HomeUiState.Success -> {
                    HomeHorizontalList(
                        upcomingMoviesUiState.data.header,
                        upcomingMoviesUiState.data.movies ?: emptyList(),
                        onItemClick
                    )
                }
                is HomeUiState.Failure -> {
                    Log.e(TAG, "${upcomingMoviesUiState.exception.message}")
                }
            }
        }
    }
}

@Composable
private fun HomeAppBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color(0xFF121212).copy(alpha = 0.9f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_launcher_44),
                contentDescription = null,
                tint = Color(0xFF9564F7)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(id = R.string.app_name),
                color = Color(0xFF9564F7),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun HomeHorizontalPager(
    movies: List<Movie>,
    onItemClick: (Int) -> Unit
) {
    val pagerState = rememberPagerState {
        movies.size
    }

    HorizontalPager(state = pagerState, modifier = Modifier.padding(start = 16.dp, end = 16.dp)) { page ->
        Card(
            modifier = Modifier.clickable { onItemClick(movies[page].id) }
        ) {
            Box(
                modifier = Modifier.background(Color(0xFF353438)).fillMaxWidth().height(230.dp)
            ) {
                if (movies[page].backdropPath.isNullOrEmpty()) {
                    Image(
                        painter = painterResource(R.drawable.ic_broken_image_24),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp).align(Alignment.Center),
                        colorFilter = ColorFilter.tint(Color.White)
                    )
                } else {
                    AsyncImage(
                        model = NetworkConstants.IMAGE_BASE_URL_W500 + movies[page].backdropPath,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Column(
                    modifier = Modifier
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFF121212).copy(alpha = 0.1f),
                                    Color(0xFF121212).copy(alpha = 0.6f))
                            )
                        )
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 20.dp)
                ) {
                    Text(
                        text = movies[page].title,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp,
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp, 16.dp),
                            tint = Color(0xFFFF97B7)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(
                                id = R.string.label_number_with_percent, (movies[page].voteAverage * 10).toInt()
                            ),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                        Text(
                            text = movies[page].releaseDate,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHorizontalList(
    headerText: String,
    movies: List<Movie>,
    onItemClick: (Int) -> Unit
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = headerText,
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        LazyRow(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(movies.size) { index ->
                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .height(286.dp)
                        .clickable { onItemClick(movies[index].id) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFF353438))
                                .fillMaxWidth()
                                .height(225.dp)
                        ) {
                            if (movies[index].posterPath.isNullOrEmpty()) {
                                Image(
                                    painter = painterResource(R.drawable.ic_broken_image_24),
                                    contentDescription = null,
                                    modifier = Modifier.size(48.dp).align(Alignment.Center),
                                    colorFilter = ColorFilter.tint(Color.White)
                                )
                            } else {
                                AsyncImage(
                                    model = NetworkConstants.IMAGE_BASE_URL_W200 + movies[index].posterPath,
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Box(
                                modifier = Modifier.align(Alignment.BottomEnd).padding(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .background(
                                            Color(0xFF121212).copy(alpha = 0.6f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(start = 4.dp, end = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(12.dp, 12.dp),
                                        tint = Color(0xFFFF97B7)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stringResource(
                                            id = R.string.label_number_with_percent,
                                            (movies[index].voteAverage * 10).toInt()
                                        ),
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        Text(
                            text = movies[index].title,
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}