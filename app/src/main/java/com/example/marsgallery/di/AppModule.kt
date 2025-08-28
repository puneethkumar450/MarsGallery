package com.example.marsgallery.di


import com.example.marsgallery.data.PhotoRepository
import com.example.marsgallery.Constants
import com.example.marsgallery.data.NasaApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides @Singleton
    fun provideNasaApi(retrofit: Retrofit): NasaApi =
        retrofit.create(NasaApi::class.java)

    @Provides @Singleton
    fun providePhotoRepository(api: NasaApi): PhotoRepository =
        PhotoRepository(api)
}
