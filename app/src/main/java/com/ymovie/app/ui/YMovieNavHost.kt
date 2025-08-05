package com.ymovie.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.source.MovieRemoteDataSource
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.ui.home.HomeScreen
import com.ymovie.app.ui.home.HomeViewModel
import com.ymovie.app.ui.home.HomeViewModelFactory
import com.ymovie.app.ui.search.SearchScreen
import com.ymovie.app.ui.search.SearchViewModel
import com.ymovie.app.ui.search.SearchViewModelFactory

@Composable
fun YMovieNavHost(
    navController: NavHostController,
    navRoutes: List<NavigationRoute>,
    innerPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = navRoutes[0].route,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable(route = navRoutes[0].route) {
            val repository = MovieRepository(
                MovieRemoteDataSource(RetrofitApiClient.retrofitInstance.create(MovieService::class.java))
            )
            val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(repository))

            HomeScreen(homeViewModel)
        }
        composable(route = navRoutes[1].route) {
            val repository = MovieRepository(
                MovieRemoteDataSource(RetrofitApiClient.retrofitInstance.create(MovieService::class.java))
            )
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModelFactory(repository))

            SearchScreen(searchViewModel)
        }
    }
}