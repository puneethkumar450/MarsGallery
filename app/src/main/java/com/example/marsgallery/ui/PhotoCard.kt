package com.example.marsgallery.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.marsgallery.data.MarsPhoto

@Composable
fun PhotoCard(photo: MarsPhoto,  onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClick() },
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            AsyncImage(
                model = photo.img_src,
                contentDescription = "Mars photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(8.dp))
            Text("Rover: ${photo.rover.name}", style = MaterialTheme.typography.titleMedium)
            Text("Camera: ${photo.camera.full_name}", style = MaterialTheme.typography.bodyMedium)
            Text("Earth Date: ${photo.earth_date}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
