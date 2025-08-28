package com.example.marsgallery

import com.example.marsgallery.data.Camera
import com.example.marsgallery.data.MarsPhoto
import com.example.marsgallery.data.NasaApi
import com.example.marsgallery.data.PhotoRepository
import com.example.marsgallery.data.PhotoResponse
import com.example.marsgallery.data.Rover
import com.example.marsgallery.ui.PhotoViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PhotoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcher()

    private val fakeRepository = FakePhotoRepository()

    @Test
    fun loadPhotos_updatesState() = runTest {
        val viewModel = PhotoViewModel(fakeRepository)

        // state should be empty at start
        assertEquals(0, viewModel.uiState.value.photos.size)

        // trigger load
        viewModel.refresh()
        advanceUntilIdle()

        // get latest value
        assertEquals(1, viewModel.uiState.value.photos.size)
        assertEquals("FHAZ", viewModel.uiState.value.photos.first().camera.name)
    }
}


// Create a fake repository to avoid real network calls
class FakePhotoRepository : PhotoRepository(
    nasaApi = FakeNasaApi()
)

// Fake API that always returns 1 photo
class FakeNasaApi : NasaApi {
    override suspend fun getMarsPhotos(
        rover: String,
        sol: Int,
        page: Int,
        apiKey: String
    ): PhotoResponse {
        return PhotoResponse(
            photos = listOf(
                MarsPhoto(
                    id = 1,
                    img_src = "https://fake.url/photo.jpg",
                    earth_date = "2025-08-27",
                    camera = Camera("FHAZ", "Front Hazard"),
                    rover = Rover("Curiosity")
                )
            )
        )
    }
}
