package com.example.mediadiary.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mediadiary.ui.collection.CollectionDestination
import com.example.mediadiary.ui.navigation.MediaDiaryNavHost
import com.example.mediadiary.ui.search.SearchDestination


sealed class Screen {
    object Search : Screen()
    object Collection : Screen()
}

data class NavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDiaryApp() {
    val navController = rememberNavController()
    val navItems = listOf(
        NavItem(SearchDestination.route, "Поиск", Icons.Default.Search),
        NavItem(CollectionDestination.route, "Коллекция", Icons.Default.Favorite)
    )

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route ?: SearchDestination.route

            NavigationBar {
                navItems.forEach { item ->
                    NavigationBarItem(
                        selected = currentRoute == item.route,
                        onClick = {
                            if(currentRoute != item.route){
                                navController.navigate(item.route)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(item.title)
                        }
                    )
                }
            }
        }
    ) { contentPadding ->
        MediaDiaryNavHost(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            contentPadding = contentPadding
        )
    }
}