package ru.magnum.mediadiary.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import ru.magnum.mediadiary.presentation.collection.CollectionsScreen
import ru.magnum.mediadiary.presentation.details.MediaDetailsWrapper
import ru.magnum.mediadiary.presentation.search.SearchScreen
import ru.magnum.mediadiary.presentation.statistics.StatisticsScreen


sealed interface AppRoute {
    @Serializable
    data object Search: AppRoute

    @Serializable
    data object Collection: AppRoute

    @Serializable
    data object Statistics: AppRoute

    @Serializable
    data class MediaDetail(
        val mediaId: Int
    ): AppRoute
}

@Composable
fun MediaDiaryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Search,
        modifier = modifier
    ) {
        composable<AppRoute.Search> {
                SearchScreen(
                    modifier = Modifier,
                    contentPadding = contentPadding,
                    onItemClick = { mediaId ->
                        navController.navigate(
                            AppRoute.MediaDetail(mediaId = mediaId)
                        )
                    }
                )
        }
        composable<AppRoute.Collection> {
            CollectionsScreen(
                contentPadding = contentPadding,
                onCollectionItemClick = { collectionItemId ->
                    navController.navigate(
                        AppRoute.MediaDetail(mediaId = collectionItemId)
                    )
                }
            )
        }
        composable<AppRoute.MediaDetail> { backStackEntry ->
            val route = backStackEntry.toRoute<AppRoute.MediaDetail>()

            MediaDetailsWrapper(
                mediaId = route.mediaId,
                onBack = { navController.popBackStack() }
            )
        }
        composable<AppRoute.Statistics> {
            StatisticsScreen(
                contentPadding = contentPadding
            )
        }
    }
}