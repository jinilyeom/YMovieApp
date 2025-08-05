package com.ymovie.app.ui

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ymovie.app.R

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
        YMovieNavHost(navController, navRoutes, innerPadding)
    }
}

sealed class NavigationRoute(
    val name: String, val route: String, var iconId: Int
) {
    data object Home : NavigationRoute("Home", "Home", R.drawable.ic_navigation_home_24)
    data object Search : NavigationRoute("Search", "Search", R.drawable.ic_navigation_search_24)
}