package com.example.mediadiary.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mediadiary.ui.collection.CollectionDestination
import com.example.mediadiary.ui.collection.CollectionsScreen
import com.example.mediadiary.ui.details.MediaDetailDestination
import com.example.mediadiary.ui.details.MediaDetailsWrapper
import com.example.mediadiary.ui.search.SearchDestination
import com.example.mediadiary.ui.search.SearchScreen
import com.example.mediadiary.ui.statistics.StatisticsDestination
import com.example.mediadiary.ui.statistics.StatisticsScreen

@Composable
fun MediaDiaryNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    NavHost(
        navController = navController,
        startDestination = SearchDestination.route,
        modifier = modifier
    ) {
        composable(route = SearchDestination.route){
            SearchScreen(
                modifier = Modifier,
                contentPadding = contentPadding,
                onItemClick = {mediaId ->
                    Log.d("DEBUGGING", "clicked mediaId = $mediaId")
                    navController.navigate("media_detail/$mediaId")
                })
        }
        composable(route = CollectionDestination.route) {
            CollectionsScreen(
                contentPadding = contentPadding,
                onCollectionItemClick = {collectionItemId ->
                    Log.d("DEBUGGING", "clicked mediaId = $collectionItemId")
                    navController.navigate("media_detail/$collectionItemId")
                }
            )
        }
        composable(route = MediaDetailDestination.route,
            arguments = listOf(navArgument(MediaDetailDestination.MEDIA_ID) {type = NavType.IntType}))
        { backStackEntry ->
            val mediaItemId = backStackEntry.arguments?.getInt(MediaDetailDestination.MEDIA_ID) ?: 0
            if(mediaItemId != 0){
                MediaDetailsWrapper(mediaId = mediaItemId, onBack = {navController.popBackStack()})
            }
        }
        composable(route = StatisticsDestination.route) {
            StatisticsScreen()
        }
    }
}