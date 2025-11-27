package com.example.mediadiary.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mediadiary.R
import com.example.mediadiary.ui.navigation.MediaDiaryNavHost
import com.example.mediadiary.ui.navigation.Screen


data class NavItem(
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDiaryApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            MediaDiaryBottomBar(navController, currentRoute)
        }
    ) { contentPadding ->
        MediaDiaryNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        )
    }
}

@Composable
fun MediaDiaryBottomBar(navController: NavController, currentRoute: String?) {
    val showBottomBar = when (currentRoute) {
        Screen.Search.route,
        Screen.Collection.route,
        Screen.Statistics.route -> true

        else -> false
    }

    if (!showBottomBar) return

    val navItems = listOf(
        NavItem(Screen.Search.route, R.string.search_screen, Icons.Default.Search),
        NavItem(Screen.Collection.route, R.string.collection_screen, Icons.Default.Favorite),
        NavItem(Screen.Statistics.route, R.string.statistics_screen, Icons.Default.BarChart)
    )
    NavigationBar {
        navItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(item.title)
                    )
                },
                label = {
                    Text(stringResource(item.title))
                }
            )
        }
    }
}