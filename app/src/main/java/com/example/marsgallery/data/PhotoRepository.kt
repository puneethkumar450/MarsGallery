package com.example.marsgallery.data

import com.example.marsgallery.Constants
import javax.inject.Inject

open class PhotoRepository @Inject constructor(val nasaApi: NasaApi) {
    suspend fun getMarsPhotos(
        rover: String,
        sol: Int = Constants.DEFAULT_SOL,
        page: Int = 1,
        apiKey: String = Constants.API_KEY
    ): PhotoResponse = nasaApi.getMarsPhotos(
        rover = rover.lowercase(),
        sol = sol,
        page = page, //25 items per page returned
        apiKey = apiKey
    )
}
