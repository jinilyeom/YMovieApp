package com.ymovie.app.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.ymovie.app.R
import com.ymovie.app.data.model.movie.Cast
import com.ymovie.app.data.model.movie.Crew
import com.ymovie.app.data.model.movie.MovieDetail
import com.ymovie.app.network.NetworkConstants

private val collapsedTopBarHeight = 64.dp
private val expandedTopBarHeight = 232.dp

@Composable
fun MovieDetailScreen(movieDetailViewModel: MovieDetailViewModel, onBackClick: () -> Unit) {
    val movieDetailUiState by movieDetailViewModel.movieDetail.collectAsStateWithLifecycle()
    val movieCreditUiState by movieDetailViewModel.movieCredit.collectAsStateWithLifecycle()

    MovieDetailScreen(movieDetailUiState, movieCreditUiState, onBackClick)
}

@Composable
private fun MovieDetailScreen(
    movieDetailUiState: MovieDetailUiState,
    movieCreditUiState: MovieCreditUiState,
    onBackClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()

    val overlapHeightPx = with(LocalDensity.current) {
        expandedTopBarHeight.toPx() - collapsedTopBarHeight.toPx()
    }

    val isCollapsed: Boolean by remember {
        derivedStateOf {
            val isFirstItemHidden = lazyListState.firstVisibleItemScrollOffset > overlapHeightPx
            isFirstItemHidden || lazyListState.firstVisibleItemIndex > 0
        }
    }

    Box {
        MovieDetailContents(lazyListState, movieDetailUiState, movieCreditUiState, onBackClick)
        MovieDetailCollapsedTopBar(movieDetailUiState, isCollapsed, onBackClick)
    }
}

@Composable
private fun MovieDetailCollapsedTopBar(
    movieDetailUiState: MovieDetailUiState,
    isCollapsed: Boolean,
    onBackClick: () -> Unit
) {
    val color: Color by animateColorAsState(
        if (isCollapsed) Color.White else Color.Transparent, label = ""
    )

    AnimatedVisibility(
        visible = isCollapsed,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(500))
    ) {
        Box(
            modifier = Modifier
                .background(color)
                .fillMaxWidth()
                .height(collapsedTopBarHeight)
        ) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }

            when (movieDetailUiState) {
                is MovieDetailUiState.Loading -> {}
                is MovieDetailUiState.Success -> {
                    Text(
                        text = movieDetailUiState.data.originalTitle,
                        modifier = Modifier.align(Alignment.Center),
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
                is MovieDetailUiState.Failure -> {}
            }
        }
    }
}

@Composable
private fun MovieDetailExpandedTopBar(
    movieDetailUiState: MovieDetailUiState,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(expandedTopBarHeight)
    ) {
        when (movieDetailUiState) {
            is MovieDetailUiState.Loading -> {}
            is MovieDetailUiState.Success -> {
                AsyncImage(
                    model = NetworkConstants.IMAGE_BASE_URL_W500 + movieDetailUiState.data.backdropPath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth()
                )
                AsyncImage(
                    model = NetworkConstants.IMAGE_BASE_URL_W200 + movieDetailUiState.data.posterPath,
                    contentDescription = null,
                    modifier = Modifier.align(alignment = Alignment.BottomStart)
                )
            }
            is MovieDetailUiState.Failure -> {}
        }

        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
        }
    }
}

@Composable
private fun MovieDetailContents(
    lazyListState: LazyListState,
    movieDetailUiState: MovieDetailUiState,
    movieCreditUiState: MovieCreditUiState,
    onBackClick: () -> Unit
) {
    LazyColumn(state = lazyListState) {
        item {
            MovieDetailExpandedTopBar(movieDetailUiState, onBackClick)
        }

        item {
            when (movieDetailUiState) {
                is MovieDetailUiState.Loading -> {}
                is MovieDetailUiState.Success -> {
                    MovieDetailBasics(movieDetailUiState.data)
                }
                is MovieDetailUiState.Failure -> {}
            }
        }

        item {
            when (movieCreditUiState) {
                is MovieCreditUiState.Loading -> {}
                is MovieCreditUiState.Success -> {
                    MovieDetailCredits(movieCreditUiState.data.casts, movieCreditUiState.data.crews)
                }
                is MovieCreditUiState.Failure -> {}
            }
        }
    }
}

@Composable
private fun MovieDetailBasics(data: MovieDetail) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 20.dp)
    ) {
        Text(
            text = data.originalTitle,
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 26.sp
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${stringResource(id = R.string.label_user_score)}: ${stringResource(id = R.string.label_number_with_percent, (data.voteAverage * 10).toInt())}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Row {
                Text(text = data.releaseDate, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = data.runtime.let { time ->
                        val hour = time / 60
                        val minute = time % 60

                        if (time > 60) {
                            stringResource(R.string.label_runtime_hour_minute, hour, minute)
                        } else if (time == 60) {
                            stringResource(R.string.label_runtime_hour, hour)
                        } else {
                            stringResource(R.string.label_runtime_minute, minute)
                        }
                    },
                    fontSize = 14.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(id = R.string.label_overview),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = data.overview, fontSize = 14.sp, lineHeight = 16.sp)
    }
}

@Composable
private fun MovieDetailCredits(casts: List<Cast>, crews: List<Crew>) {
    Spacer(modifier = Modifier.height(20.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .padding(start = 16.dp, end = 16.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = stringResource(R.string.label_top_cast),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
    ) {
        items(casts.size) { index ->
            Card(
                modifier = Modifier.size(122.dp, 249.dp)
            ) {
                AsyncImage(
                    model = NetworkConstants.IMAGE_BASE_URL_W200 + casts[index].profilePath,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(133.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = casts[index].name,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                    Text(
                        text = casts[index].character,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 14.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(20.dp))
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.label_director),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = crews.filter {
                it.department == "Directing" && it.job == "Director"
            }.map {
                it.name
            }.let {
                val str = it.toString()
                str.substring(1, str.length - 1)
            },
            fontSize = 14.sp,
            lineHeight = 16.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.label_writer),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = crews.filter {
                it.department == "Writing"
            }.map {
                it.name
            }.let {
                val str = it.toString()
                str.substring(1, str.length - 1)
            },
            fontSize = 14.sp,
            lineHeight = 16.sp
        )
    }
    Spacer(modifier = Modifier.height(20.dp))
}