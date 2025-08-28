package com.example.marsgallery.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoListScreen(viewModel: PhotoViewModel) {
    val lUiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()

    // Infinite scroll trigger if you are near the end
    LaunchedEffect(listState, lUiState.photos.size) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .filterNotNull()
            .map { lastIndex -> lastIndex >= lUiState.photos.lastIndex - 3 }
            .distinctUntilChanged()
            .collectLatest { shouldLoadMore ->
                if (shouldLoadMore) viewModel.loadMore()
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${lUiState.selectedRover.label} Photos") }
            )
        },
        bottomBar = {
            NavigationBar {
                RoverTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = lUiState.selectedRover == tab,
                        onClick = { viewModel.selectRover(tab) },
                        icon = { Icon(Icons.Default.Add, contentDescription = null) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = lUiState.isRefreshing),
            onRefresh = { viewModel.refresh() },
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            // Empty state
            if (lUiState.photos.isEmpty() && !lUiState.isRefreshing && lUiState.error == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No photos found.")
                }
            } else {
                LazyColumn(
                    state = listState,
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    itemsIndexed(lUiState.photos, key = { _, item -> item }) { index, photo ->
                        PhotoCard(photo)
                        if (index == lUiState.photos.lastIndex) Spacer(Modifier.height(7.dp))
                    }

                    if (lUiState.isLoadingMore) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
            }
        }
    }
}
