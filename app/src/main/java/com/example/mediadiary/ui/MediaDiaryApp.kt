package com.example.mediadiary.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mediadiary.ui.search.SearchScreen
import com.example.mediadiary.ui.search.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaDiaryApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { contentPadding ->
        Surface(
            modifier = Modifier.fillMaxSize().padding(contentPadding)
        ) {
            val searchViewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
            SearchScreen(searchViewModel, modifier = Modifier.fillMaxSize())
        }
    }
}