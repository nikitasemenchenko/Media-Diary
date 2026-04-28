package ru.magnum.mediadiary.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.magnum.mediadiary.presentation.collection.CollectionsScreen
import ru.magnum.mediadiary.presentation.details.MediaDetailsWrapper
import ru.magnum.mediadiary.presentation.search.SearchScreen
import ru.magnum.mediadiary.presentation.statistics.StatisticsScreen


sealed class Screen(
    val route: String
) {
    object Search : Screen("search")
    object Collection : Screen("collection")
    object Statistics : Screen("statistics")
    data class MediaDetail(val mediaId: Int) : Screen("media_detail/{$ARG}") {
        companion object {
            const val ARG = "mediaId"
            const val ROUTE = "media_detail/{$ARG}"
        }
        fun createRoute() = "media_detail/$mediaId"
    }
}

@Composable
fun MediaDiaryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Search.route,
        modifier = modifier
    ) {
        composable(
            route = Screen.Search.route
        ) {
                SearchScreen(
                    modifier = Modifier,
                    contentPadding = contentPadding,
                    onItemClick = { mediaId ->
                        navController.navigate(Screen.MediaDetail(mediaId).createRoute())
                    }
                )
        }
        composable(
            route = Screen.Collection.route
        ) {
            CollectionsScreen(
                contentPadding = contentPadding,
                onCollectionItemClick = { collectionItemId ->
                    navController.navigate(
                        Screen.MediaDetail(collectionItemId).createRoute()
                    )
                }
            )
        }
        composable(
            route = Screen.MediaDetail.ROUTE,
            arguments = listOf(navArgument(Screen.MediaDetail.ARG) {
                type = NavType.IntType
                }
            )
        )
        { backStackEntry ->
            val mediaItemId = backStackEntry.arguments?.getInt(Screen.MediaDetail.ARG) ?: 0
            if (mediaItemId != 0) {
                MediaDetailsWrapper(
                    mediaId = mediaItemId,
                    onBack = { navController.popBackStack() })
            }
        }
        composable(
            route = Screen.Statistics.route
        ) {
            StatisticsScreen(
                contentPadding = contentPadding
            )
        }
    }
}