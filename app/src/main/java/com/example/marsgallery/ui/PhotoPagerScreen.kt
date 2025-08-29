package com.example.marsgallery.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.marsgallery.data.MarsPhoto

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoPagerScreen(
    photos: List<MarsPhoto>,
    startIndex: Int,
    navController: NavHostController
) {
    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { photos.size }
    )

    Column(modifier = Modifier.fillMaxSize().padding(top = 50.dp, bottom = 60.dp)) {
        Button(onClick = { navController.popBackStack() },
            modifier = Modifier.padding(8.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            val photo = photos[page]
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = photo.img_src,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().weight(1f)
                )
                Text("Camera: ${photo.camera.name}")
                Text("Rover: ${photo.rover.name}")
                Text("Date: ${photo.earth_date}")
            }
        }
    }
}
