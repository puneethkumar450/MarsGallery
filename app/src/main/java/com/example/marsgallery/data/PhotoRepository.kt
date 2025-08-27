package com.example.marsgallery.data

import com.example.marsgallery.Constants
import javax.inject.Inject

class PhotoRepository @Inject constructor(
    private val api: NasaApi
) {
    suspend fun getMarsPhotos(
        rover: String,
        sol: Int = Constants.DEFAULT_SOL,
        page: Int = 1,
        apiKey: String = Constants.API_KEY
    ): PhotoResponse = api.getMarsPhotos(
        rover = rover.lowercase(),
        sol = sol,
        page = page, //25 items per page returned
        apiKey = apiKey
    )
}
