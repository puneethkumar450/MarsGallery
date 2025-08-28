package com.example.marsgallery.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import coil.compose.AsyncImage
import com.example.marsgallery.data.MarsPhoto

@Composable
fun PhotoListScreenTest(
    photos: List<MarsPhoto>,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(photos) { photo ->
                Card(
                    modifier = Modifier
                        .padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    AsyncImage(
                        model = photo.img_src,
                        contentDescription = "Mars photo",
                        modifier = Modifier.fillMaxSize()
                    )
                    Text(
                        text = "Rover: ${photo.rover.name}",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Date: ${photo.earth_date}",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}
