package com.example.marsgallery.data


import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
data class PhotoResponse(val photos: List<MarsPhoto>)

@Parcelize
data class MarsPhoto(
    val id: Long,
    val img_src: String,
    val earth_date: String,
    val camera: Camera,
    val rover: Rover
) : Parcelable

@Parcelize
data class Camera(
    val name: String,
    val full_name: String
) : Parcelable

@Parcelize
data class Rover(
    val name: String
) : Parcelable


interface NasaApi {
    @GET("mars-photos/api/v1/rovers/{rover}/photos")
    suspend fun getMarsPhotos(
        @Path("rover") rover: String,
        @Query("sol") sol: Int,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): PhotoResponse
}


