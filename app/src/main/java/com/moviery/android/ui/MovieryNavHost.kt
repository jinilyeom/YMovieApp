package com.moviery.android.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.moviery.android.data.MovieRepository
import com.moviery.android.data.model.HomeReqParam
import com.moviery.android.data.model.MovieDetailReqParam
import com.moviery.android.data.source.MovieRemoteDataSource
import com.moviery.android.network.RetrofitClient
import com.moviery.android.network.service.MovieService
import com.moviery.android.ui.detail.MovieDetailNavigation
import com.moviery.android.ui.detail.MovieDetailScreen
import com.moviery.android.ui.detail.MovieDetailViewModel
import com.moviery.android.ui.detail.MovieDetailViewModelFactory
import com.moviery.android.ui.home.HomeScreen
import com.moviery.android.ui.home.HomeViewModel
import com.moviery.android.ui.home.HomeViewModelFactory
import com.moviery.android.ui.search.SearchScreen
import com.moviery.android.ui.search.SearchViewModel
import com.moviery.android.ui.search.SearchViewModelFactory

@Composable
fun MovieryNavHost(
    appState: MovieryAppState,
    navController: NavHostController,
    navRoutes: List<NavigationRoute>,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = navRoutes[0].route
    ) {
        composable(route = navRoutes[0].route) {
            val repository = MovieRepository(
                MovieRemoteDataSource(RetrofitClient.instance.create(MovieService::class.java))
            )
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(repository))
            homeViewModel.setHomeReqParam(HomeReqParam(appState.appLanguage))

            HomeScreen(
                homeViewModel,
                onItemClick = { movieId ->
                    navController.navigate(MovieDetailNavigation(movieId)) {
                        launchSingleTop = true
                    }
                },
                innerPadding
            )
        }
        composable(route = navRoutes[1].route) {
            val repository = MovieRepository(
                MovieRemoteDataSource(RetrofitClient.instance.create(MovieService::class.java))
            )
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModelFactory(repository))

            SearchScreen(
                appState,
                searchViewModel,
                onItemClick = { movieId ->
                    navController.navigate(MovieDetailNavigation(movieId)) {
                        launchSingleTop = true
                    }
                },
                innerPadding
            )
        }
        composable<MovieDetailNavigation> { backStackEntry ->
            val movieDetailNavigation: MovieDetailNavigation = backStackEntry.toRoute()
            val repository = MovieRepository(
                MovieRemoteDataSource(RetrofitClient.instance.create(MovieService::class.java))
            )
            val movieDetailViewModel: MovieDetailViewModel = viewModel(factory = MovieDetailViewModelFactory(repository))
            movieDetailViewModel.setMovieDetailReqParam(MovieDetailReqParam(movieDetailNavigation.movieId, appState.appLanguage))

            MovieDetailScreen(
                movieDetailViewModel,
                onBackClick = {
                    navController.popBackStack()
                },
                innerPadding
            )
        }
    }
}