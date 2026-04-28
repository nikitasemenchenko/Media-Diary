package ru.magnum.mediadiary.presentation

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.magnum.mediadiary.R
import ru.magnum.mediadiary.presentation.navigation.AppRoute
import ru.magnum.mediadiary.presentation.navigation.MediaDiaryNavHost


data class NavItem(
    val route: AppRoute,
    @StringRes val title: Int,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDiaryApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination

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
fun MediaDiaryBottomBar(
    navController: NavController,
    currentRoute: NavDestination?
) {
    val navItems = listOf(
        NavItem(
            route = AppRoute.Search,
            title = R.string.search_screen,
            icon = Icons.Default.Search
        ),
        NavItem(
            route = AppRoute.Collection,
            title = R.string.collection_screen,
            icon = Icons.Default.Favorite
        ),
        NavItem(
            route = AppRoute.Statistics,
            title = R.string.statistics_screen,
            icon = Icons.Default.BarChart
        )
    )

    val showBottomBar = navItems.any { item ->
        currentRoute?.hierarchy?.any { destination ->
            destination.hasRoute(item.route::class)
        } == true
    }

    if (!showBottomBar) return

    NavigationBar(
        tonalElevation = 8.dp
    ) {
        navItems.forEach { item ->

            val selected = currentRoute?.hierarchy?.any { destination ->
                destination.hasRoute(item.route::class)
            } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
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
                    Text(
                        text = stringResource(item.title),
                        maxLines = 1
                    )
                }
            )
        }
    }
}