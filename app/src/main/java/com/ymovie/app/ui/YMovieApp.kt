package com.ymovie.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ymovie.app.R
import com.ymovie.app.data.MovieRepository
import com.ymovie.app.data.source.MovieRemoteDataSource
import com.ymovie.app.network.RetrofitApiClient
import com.ymovie.app.network.service.MovieService
import com.ymovie.app.ui.home.HomeScreen
import com.ymovie.app.ui.home.HomeViewModel
import com.ymovie.app.ui.home.HomeViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YMovieApp() {
    val navController = rememberNavController()
    val navRoutes = listOf(NavigationRoute.Home, NavigationRoute.Search)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) }
            )
        },
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                navRoutes.forEach { navRoute ->
                    NavigationBarItem(
                        selected = currentRoute == navRoute.route,
                        onClick = {
                            navController.navigate(navRoute.route) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(id = navRoute.iconId),
                                contentDescription = navRoute.name
                            )
                        },
                        label = { Text(text = navRoute.name) }
                    )
                }
            }
        }
    ) { innerPadding ->
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
                // Search
            }
        }
    }
}

sealed class NavigationRoute(
    val name: String, val route: String, val iconId: Int
) {
    data object Home : NavigationRoute("Home", "Home", R.drawable.ic_navigation_home_24)
    data object Search : NavigationRoute("Search", "Search", R.drawable.ic_navigation_search_24)
}