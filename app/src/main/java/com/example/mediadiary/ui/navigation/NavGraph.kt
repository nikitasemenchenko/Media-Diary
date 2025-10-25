package com.example.mediadiary.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mediadiary.ui.collection.CollectionDestination
import com.example.mediadiary.ui.collection.CollectionsScreen
import com.example.mediadiary.ui.search.SearchDestination
import com.example.mediadiary.ui.search.SearchScreen

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
                contentPadding = contentPadding)
        }
        composable(route = CollectionDestination.route) {
            CollectionsScreen(
                contentPadding = contentPadding
            )
        }
    }
}