package com.example.marsgallery

import com.example.marsgallery.data.NasaApi
import com.example.marsgallery.data.PhotoResponse
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Test

import org.junit.Assert.*
import retrofit2.Retrofit

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApiTest {
    private val api: NasaApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .client(OkHttpClient.Builder().build())
            .build()
            .create(NasaApi::class.java)
    }

    @Test
    fun `fetch photos returns non empty list`() = runTest {
        val response: PhotoResponse =
            api.getMarsPhotos(
                rover = "curiosity", sol = 1000, apiKey = "DEMO_KEY", page = 1
            )
        assertTrue("Expected photos but got empty", response.photos.isNotEmpty())
    }
}