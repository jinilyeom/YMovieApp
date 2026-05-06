package com.ymovie.app.ui.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
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
private val expandedTopBarHeight = 619.dp

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

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
        MovieDetailContents(lazyListState, movieDetailUiState, movieCreditUiState, onBackClick)
        MovieDetailCollapsedAppBar(movieDetailUiState, isCollapsed, onBackClick)
    }
}

@Composable
private fun MovieDetailCollapsedAppBar(
    movieDetailUiState: MovieDetailUiState,
    isCollapsed: Boolean,
    onBackClick: () -> Unit
) {
    val color: Color by animateColorAsState(
        if (isCollapsed) Color.Black.copy(alpha = 0.6f) else Color.Transparent, label = ""
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
                .padding(horizontal = 16.dp)
        ) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier.background(Color.Black.copy(alpha = 0.6f), CircleShape).align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White
                )
            }

            when (movieDetailUiState) {
                is MovieDetailUiState.Loading -> {}
                is MovieDetailUiState.Success -> {
                    Text(
                        text = movieDetailUiState.data.originalTitle,
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
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
private fun MovieDetailContents(
    lazyListState: LazyListState,
    movieDetailUiState: MovieDetailUiState,
    movieCreditUiState: MovieCreditUiState,
    onBackClick: () -> Unit
) {
    LazyColumn(state = lazyListState) {
        item {
            when (movieDetailUiState) {
                is MovieDetailUiState.Loading -> {}
                is MovieDetailUiState.Success -> {
                    MovieDetailBasics(movieDetailUiState.data, onBackClick)
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
private fun MovieDetailBasics(data: MovieDetail, onBackClick: () -> Unit) {
    Column {
        Box(
            modifier = Modifier.background(Color(0xFF353438)).fillMaxWidth().height(expandedTopBarHeight)
        ) {
            if (data.posterPath.isNullOrEmpty()) {
                Image(
                    painter = painterResource(R.drawable.ic_broken_image_24),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp).align(Alignment.Center),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            } else {
                AsyncImage(
                    model = NetworkConstants.IMAGE_BASE_URL_W500 + data.posterPath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            Box(modifier = Modifier.fillMaxWidth().padding(start = 16.dp, top = 12.dp, end = 16.dp)) {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.6f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.verticalGradient(listOf(Color.Black.copy(alpha = 0.1f), Color.Black.copy(alpha = 0.6f))))
                    .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 20.dp)
                    .align(Alignment.BottomStart)
            ) {
                Text(
                    text = data.originalTitle,
                    modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp, 16.dp),
                            tint = Color(0xFFFF97B7)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.label_number_with_percent, (data.voteAverage * 10).toInt()),
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    Text(text = data.releaseDate, color = Color.White, fontSize = 16.sp)
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
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.label_overview),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = data.overview,
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun MovieDetailCredits(casts: List<Cast>, crews: List<Crew>) {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth().height(45.dp).padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = stringResource(R.string.label_top_cast),
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)
        ) {
            items(casts.size) { index ->
                Card(
                    modifier = Modifier.size(160.dp, 320.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
                ) {
                    Box(modifier = Modifier.background(Color(0xFF353438)).fillMaxWidth().height(240.dp)) {
                        if (casts[index].profilePath.isNullOrEmpty()) {
                            Image(
                                painter = painterResource(R.drawable.ic_broken_image_24),
                                contentDescription = null,
                                modifier = Modifier.size(48.dp).align(Alignment.Center),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        } else {
                            AsyncImage(
                                model = NetworkConstants.IMAGE_BASE_URL_W200 + casts[index].profilePath,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(
                            text = casts[index].name,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                        Text(
                            text = casts[index].character,
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White,
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.label_director),
                color = Color.White,
                fontSize = 18.sp,
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
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(R.string.label_writer),
                color = Color.White,
                fontSize = 18.sp,
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
                color = Color.White,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}