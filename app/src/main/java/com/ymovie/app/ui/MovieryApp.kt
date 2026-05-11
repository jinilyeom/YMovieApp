package com.ymovie.app.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ymovie.app.R

private const val APP_LANGUAGE = "ko-KR"

@Composable
fun MovieryApp() {
    val appState = remember { MovieryAppState(APP_LANGUAGE) }
    val navController = rememberNavController()
    val navRoutes = listOf(NavigationRoute.Home, NavigationRoute.Search)

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF121212)) {
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
                        label = { Text(text = navRoute.name) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            selectedTextColor = Color.White,
                            indicatorColor = Color.Transparent,
                            unselectedIconColor = Color(0xFFBBBBBB),
                            unselectedTextColor = Color(0xFFBBBBBB)
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        MovieryNavHost(appState, navController, navRoutes, innerPadding)
    }
}

sealed class NavigationRoute(
    val name: String, val route: String, var iconId: Int
) {
    data object Home : NavigationRoute("Home", "Home", R.drawable.ic_navigation_home_24)
    data object Search : NavigationRoute("Search", "Search", R.drawable.ic_navigation_search_24)
}