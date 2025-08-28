package com.example.marsgallery

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.marsgallery.data.Camera
import com.example.marsgallery.data.MarsPhoto
import com.example.marsgallery.data.Rover
import com.example.marsgallery.ui.PhotoListScreenTest
import org.junit.Rule
import org.junit.Test

class UiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()


    @get:Rule
    val mainDispatcherRule = MainDispatcher()


    @Test
    fun photos_are_displayed_in_list() {
        // Fake list of photos
        val fakePhotos = listOf(
            MarsPhoto(
                id = 1,
                img_src = "https://mars.nasa.gov/photo.jpg",
                earth_date = "2025-08-27",
                camera = Camera("FHAZ", "Front Hazard Avoidance Camera"),
                rover = Rover("Curiosity")
            )
        )

        // Render PhotoListScreen with fake data
        composeRule.setContent {
            PhotoListScreenTest(
                photos = fakePhotos,
                onRefresh = {}
            )
        }

        // Check if text from fake photo is shown
        composeRule.onNodeWithText("Curiosity").assertIsDisplayed()
    }
}
