package com.example.marsgallery

import android.util.Log

object Constants {
    const val BASE_URL = "https://api.nasa.gov/"
    // For demo only. will move to local.properties / BuildConfig for production
    const val API_KEY = "HW0SbJwOa3u4Xg5Ql2JtX3H8lcya89qb91CtRADP"
    const val DEFAULT_SOL = 1000

    // extension for logs
    fun Any.LogD(msg: String) = println(msg)
    fun Any.LogE(msg: String) = println(msg)
}